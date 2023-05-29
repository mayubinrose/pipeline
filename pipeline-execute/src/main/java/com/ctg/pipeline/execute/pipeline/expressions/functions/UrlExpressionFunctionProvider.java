package com.ctg.pipeline.execute.pipeline.expressions.functions;

import static java.lang.String.format;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import com.ctg.pipeline.execute.model.UserConfiguredUrlRestrictions;
import com.ctg.pipeline.execute.util.ExecuteObjectMapper;
import com.ctg.pipeline.execute.util.HttpClientUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.spinnaker.kork.expressions.ExpressionFunctionProvider;
import com.netflix.spinnaker.kork.expressions.SpelHelperFunctionException;

@SuppressWarnings("unused")
@Component
public class UrlExpressionFunctionProvider implements ExpressionFunctionProvider {
  private static AtomicReference<UserConfiguredUrlRestrictions> helperFunctionUrlRestrictions =
      new AtomicReference<>();
  private static final ObjectMapper mapper = ExecuteObjectMapper.getInstance();

  public UrlExpressionFunctionProvider(UserConfiguredUrlRestrictions urlRestrictions) {
    helperFunctionUrlRestrictions.set(urlRestrictions);
  }

  @Nullable
  @Override
  public String getNamespace() {
    return null;
  }

  @NotNull
  @Override
  public Functions getFunctions() {
    return new Functions(
        new FunctionDefinition(
            "fromUrl",
            "Downloads text from the given URL",
            new FunctionParameter(String.class, "url", "A URL to retrieve text from")),
        new FunctionDefinition(
            "jsonFromUrl",
            "Downloads and parses JSON from the given URL",
            new FunctionParameter(String.class, "url", "A URL to retrieve a JSON file from")),
        new FunctionDefinition(
            "yamlFromUrl",
            "Downloads and parses YAML from the given URL",
            new FunctionParameter(String.class, "url", "A URL to retrieve a YAML file from")),
        new FunctionDefinition(
            "propertiesFromUrl",
            "Downloads and parses Java style properties from the given URL",
            new FunctionParameter(String.class, "url", "A URL to retrieve a Properties file from")),
        new FunctionDefinition(
            "readJson",
            "Parses JSON from a string to be accessed, parsed JSON can be accessed as an object",
            new FunctionParameter(String.class, "value", "A String containing JSON text")),
        new FunctionDefinition(
            "readYaml",
            "Parses YAML from a string to be accessed, parsed JSON can be accessed as an object",
            new FunctionParameter(String.class, "value", "A String containing YAML text")));
  }

  /**
   * Reads a json text
   *
   * @param url url to get the json text
   * @return an object representing the json object
   */
  public static Object jsonFromUrl(String url) {
    return readJson(fromUrl(url));
  }

  /**
   * Attempts to read json from a text String. Will throw a parsing exception on bad json
   *
   * @param text text to read as json
   * @return the json representation of the text
   */
  public static Object readJson(String text) {
    try {
      if (text.startsWith("[")) {
        return mapper.readValue(text, List.class);
      }

      return mapper.readValue(text, Map.class);
    } catch (Exception e) {
      throw new SpelHelperFunctionException(format("#readJson(%s) failed", text), e);
    }
  }

  /**
   * Reads a yaml text
   *
   * @param url url to get the json text
   * @return an object representing the yaml object
   */
  public static Object yamlFromUrl(String url) {
    return readYaml(fromUrl(url));
  }

  /**
   * Attempts to read yaml from a text String. Will throw a parsing exception on bad yaml
   *
   * @param text text to read as yaml
   * @return the object representation of the yaml text
   */
  public static Object readYaml(String text) {
    try {
      return new Yaml().load(text);
    } catch (Exception e) {
      throw new SpelHelperFunctionException(format("#readYaml(%s) failed", text), e);
    }
  }

  /**
   * Reads a properties file stored at a url
   *
   * @param url the location of the properties file
   * @return a hashmap representing the properties file
   */
  public static Map propertiesFromUrl(String url) {
    try {
      return readProperties(fromUrl(url));
    } catch (Exception e) {
      throw new SpelHelperFunctionException(format("#propertiesFromUrl(%s) failed", url), e);
    }
  }

  /**
   * Reads properties from a text
   *
   * @param text text
   * @return a map of the key-value pairs in the text
   * @throws IOException
   */
  static Map readProperties(String text) throws IOException {
    Properties properties = new Properties();
    properties.load(new ByteArrayInputStream(text.getBytes()));
    Map map = new HashMap(properties);
    return map;
  }

  /**
   * Returns the text response from url
   *
   * @param url used to perform the http get response
   * @return string result from get request
   */
  public static String fromUrl(String url) {
    try {
      URL u = helperFunctionUrlRestrictions.get().validateURI(url).toURL();
      return HttpClientUtils.httpGetAsString(u.toString());
    } catch (Exception e) {
      throw new SpelHelperFunctionException(format("#from(%s) failed", url), e);
    }
  }
}
