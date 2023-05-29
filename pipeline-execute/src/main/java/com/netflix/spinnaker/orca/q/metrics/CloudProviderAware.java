package com.netflix.spinnaker.orca.q.metrics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ctg.pipeline.execute.model.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public interface CloudProviderAware {
  String DEFAULT_CLOUD_PROVIDER = "aws"; // TODO: Should we fetch this from configuration instead?
  Logger cloudProviderAwareLog = LoggerFactory.getLogger(CloudProviderAware.class);

  default String getDefaultCloudProvider() {
    return DEFAULT_CLOUD_PROVIDER;
  }

  @Nullable
  default String getCloudProvider(Stage stage) {
    return getCloudProvider(stage.getContext());
  }

  @Nullable
  default String getCloudProvider(Map<String, Object> context) {
    return (String) context.getOrDefault("cloudProvider", getDefaultCloudProvider());
  }

  @Nullable
  default String getCredentials(Stage stage) {
    return getCredentials(stage.getContext());
  }

  @Nullable
  default String getCredentials(Map<String, Object> context) {
    return (String)
        context.getOrDefault(
            "account.name", context.getOrDefault("account", context.get("credentials")));
  }

  // may return a list with 0, 1 or more regions (no guarantees on the ordering)
  default List<String> getRegions(Map<String, Object> context) {
    String region = (String) context.getOrDefault("region", null);
    if (region != null) {
      return ImmutableList.of(region);
    }

    try {
      Map<String, Object> deployServerGroups =
          (Map<String, Object>) context.getOrDefault("deploy.server.groups", null);
      if (deployServerGroups == null || deployServerGroups.isEmpty()) {
        return ImmutableList.of();
      }

      Set<String> regions = (Set<String>) deployServerGroups.keySet();
      return ImmutableList.copyOf(regions);
    } catch (ClassCastException e) {
      cloudProviderAwareLog.error(
          "Failed to parse deploy.server.groups in stage context " + context, e);
      return ImmutableList.of();
    }
  }

  default List<String> getRegions(Stage stage) {
    return getRegions(stage.getContext());
  }

  default boolean hasCloudProvider(@Nonnull Stage stage) {
    return getCloudProvider(stage) != null;
  }

  default boolean hasCredentials(@Nonnull Stage stage) {
    return getCredentials(stage) != null;
  }
}
