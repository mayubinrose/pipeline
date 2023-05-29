package com.ctg.pipeline.execute.model;

import com.ctg.pipeline.execute.queue.model.DefaultTrigger;
import com.ctg.pipeline.execute.queue.model.Trigger;
import com.ctg.pipeline.execute.util.JSONDateSerial;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

import static com.ctg.pipeline.execute.model.ExecutionStatus.NOT_STARTED;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;

public class Execution implements Serializable {

    public static final Trigger NO_TRIGGER = new DefaultTrigger("none");
    private static final ULID ID_GENERATOR = new ULID();

    public Execution(ExecutionType type, String application) {
        this(type, ID_GENERATOR.nextULID(), application);
    }

    @JsonCreator
    public Execution(@JsonProperty("type") ExecutionType type, @JsonProperty("id") String id,
        @JsonProperty("application") String application) {
        this.type = type;
        this.id = id;
        this.application = application;
    }

    private final ExecutionType type;

    public @Nonnull ExecutionType getType() {
        return type;
    }

    private String id;

    public @Nonnull String getId() {
        return id;
    }

    public void setId(@Nonnull String id) {
        this.id = id;
    }

    private String application;

    public @Nonnull String getApplication() {
        return application;
    }

    public void setApplication(@Nonnull String application) {
        this.application = application;
    }

    private String name;

    public @Nullable String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @JsonSerialize(using = JSONDateSerial.class)
    private Long buildTime;

    public @Nullable Long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(@Nullable Long buildTime) {
        this.buildTime = buildTime;
    }

    private boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    private String canceledBy;

    public @Nullable String getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(@Nullable String canceledBy) {
        this.canceledBy = canceledBy;
    }

    private String cancellationReason;

    public @Nullable String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(@Nullable String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    private boolean limitConcurrent = false;

    public boolean isLimitConcurrent() {
        return limitConcurrent;
    }

    public void setLimitConcurrent(boolean limitConcurrent) {
        this.limitConcurrent = limitConcurrent;
    }

    private boolean keepWaitingPipelines = false;

    public boolean isKeepWaitingPipelines() {
        return keepWaitingPipelines;
    }

    public void setKeepWaitingPipelines(boolean keepWaitingPipelines) {
        this.keepWaitingPipelines = keepWaitingPipelines;
    }

    @JsonIgnore
    public @Nonnull Map<String, Object> getContext() {
        return Stage.topologicalSort(stages).map(Stage::getOutputs).map(Map::entrySet).flatMap(Collection::stream)
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (o, o2) -> o2));
    }

    private final List<Stage> stages = new ArrayList<>();

    /**
     * Gets the stages of this execution. Does not serialize the child Execution object from stages. The child Execution
     * object in Stage is a @JsonBackReference.
     */
    @JsonIgnoreProperties(value = "execution")
    public @Nonnull List<Stage> getStages() {
        return stages;
    }

    @JsonSerialize(using = JSONDateSerial.class)
    private Long startTime;

    public @Nullable Long getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable Long startTime) {
        this.startTime = startTime;
    }

    @JsonSerialize(using = JSONDateSerial.class)
    private Long endTime;

    public @Nullable Long getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable Long endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the start expiry timestamp for this execution. If the execution has not started before this timestamp, the
     * execution will immediately terminate.
     */
    private Long startTimeExpiry;

    public @Nullable Long getStartTimeExpiry() {
        return startTimeExpiry;
    }

    public void setStartTimeExpiry(@Nullable Long startTimeExpiry) {
        this.startTimeExpiry = startTimeExpiry;
    }

    private ExecutionStatus status = NOT_STARTED;

    public @Nonnull ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(@Nonnull ExecutionStatus status) {
        this.status = status;
    }

    private AuthenticationDetails authentication;

    public @Nullable AuthenticationDetails getAuthentication() {
        return authentication;
    }

    public void setAuthentication(@Nullable AuthenticationDetails authentication) {
        this.authentication = authentication;
    }

    private PausedDetails paused;

    public @Nullable PausedDetails getPaused() {
        return paused;
    }

    public void setPaused(@Nullable PausedDetails paused) {
        this.paused = paused;
    }

    private String origin;

    public @Nullable String getOrigin() {
        return origin;
    }

    public void setOrigin(@Nullable String origin) {
        this.origin = origin;
    }

    private Trigger trigger = NO_TRIGGER;

    public @Nonnull Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(@Nonnull Trigger trigger) {
        this.trigger = trigger;
    }

    private String description;

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    private String pipelineConfigId;

    public @Nullable String getPipelineConfigId() {
        return pipelineConfigId;
    }

    public void setPipelineConfigId(@Nullable String pipelineConfigId) {
        this.pipelineConfigId = pipelineConfigId;
    }

    private PipelineSource source;

    public @Nullable PipelineSource getSource() {
        return source;
    }

    public void setSource(@Nullable PipelineSource source) {
        this.source = source;
    }

    private List<Map<String, Object>> notifications = new ArrayList<>();

    public @Nonnull List<Map<String, Object>> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Map<String, Object>> notifications) {
        this.notifications = notifications;
    }

    private final Map<String, Object> initialConfig = new HashMap<>();

    public @Nonnull Map<String, Object> getInitialConfig() {
        return initialConfig;
    }

    private final List<SystemNotification> systemNotifications = new ArrayList<>();

    public @Nonnull List<SystemNotification> getSystemNotifications() {
        return systemNotifications;
    }

    private String spelEvaluator;

    public @Nullable String getSpelEvaluator() {
        return spelEvaluator;
    }

    public void setSpelEvaluator(@Nullable String spelEvaluatorVersion) {
        this.spelEvaluator = spelEvaluatorVersion;
    }

    private Map<String, Object> templateVariables = null;

    public @Nullable Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(@Nullable Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables;
    }

    public String partition = null;

    public void setPartition(@Nullable String partition) {
        this.partition = partition;
    }

    public @Nullable String getPartition() {
        return this.partition;
    }

    @Nullable
    public Stage namedStage(String type) {
        return stages.stream().filter(it -> it.getType().equals(type)).findFirst().orElse(null);
    }

    @Nonnull
    public Stage stageById(String stageId) {
        return stages.stream().filter(it -> it.getId().equals(stageId)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("No stage with id %s exists", stageId)));
    }

    @Nonnull
    public Stage stageByRef(String refId) {
        return stages.stream().filter(it -> refId.equals(it.getRefId())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("No stage with refId %s exists", refId)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Execution execution = (Execution)o;
        return Objects.equals(id, execution.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Execution newOrchestration(String application) {
        return new Execution(ExecutionType.ORCHESTRATION, application);
    }

    public static Execution newPipeline(String application) {
        return new Execution(ExecutionType.PIPELINE, application);
    }

    public static class AuthenticationDetails {

        private String user;

        public @Nullable String getUser() {
            return user;
        }

        public void setUser(@Nullable String user) {
            this.user = user;
        }

        private Collection<String> allowedAccounts = emptySet();

        public Collection<String> getAllowedAccounts() {
            return ImmutableSet.copyOf(allowedAccounts);
        }

        public void setAllowedAccounts(Collection<String> allowedAccounts) {
            this.allowedAccounts = ImmutableSet.copyOf(allowedAccounts);
        }

        public AuthenticationDetails() {}

        public AuthenticationDetails(String user, String... allowedAccounts) {
            this.user = user;
            this.allowedAccounts = asList(allowedAccounts);
        }

        public static Optional<AuthenticationDetails> build() {
            Optional<String> spinnakerUserOptional = Optional.ofNullable(null);
            Optional<String> spinnakerAccountsOptional = Optional.empty();
            if (spinnakerUserOptional.isPresent() || spinnakerAccountsOptional.isPresent()) {
                return Optional.of(new AuthenticationDetails(spinnakerUserOptional.orElse(null),
                    spinnakerAccountsOptional.map(s -> s.split(",")).orElse(new String[0])));
            }

            return Optional.empty();
        }

    }

    public static class PausedDetails {
        String pausedBy;

        public @Nullable String getPausedBy() {
            return pausedBy;
        }

        public void setPausedBy(@Nullable String pausedBy) {
            this.pausedBy = pausedBy;
        }

        String resumedBy;

        public @Nullable String getResumedBy() {
            return resumedBy;
        }

        public void setResumedBy(@Nullable String resumedBy) {
            this.resumedBy = resumedBy;
        }

        @JsonSerialize(using = JSONDateSerial.class)
        Long pauseTime;

        public @Nullable Long getPauseTime() {
            return pauseTime;
        }

        public void setPauseTime(@Nullable Long pauseTime) {
            this.pauseTime = pauseTime;
        }

        @JsonSerialize(using = JSONDateSerial.class)
        Long resumeTime;

        public @Nullable Long getResumeTime() {
            return resumeTime;
        }

        public void setResumeTime(@Nullable Long resumeTime) {
            this.resumeTime = resumeTime;
        }

        @JsonIgnore
        public boolean isPaused() {
            return pauseTime != null && resumeTime == null;
        }

        @JsonIgnore
        public long getPausedMs() {
            return (pauseTime != null && resumeTime != null) ? resumeTime - pauseTime : 0;
        }
    }

    public enum ExecutionType {
        PIPELINE, ORCHESTRATION;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static class PipelineSource {
        private String id;

        public @Nonnull String getId() {
            return id;
        }

        public void setId(@Nonnull String id) {
            this.id = id;
        }

        private String type;

        public @Nonnull String getType() {
            return type;
        }

        public void setType(@Nonnull String type) {
            this.type = type;
        }

        private String version;

        public @Nonnull String getVersion() {
            return version;
        }

        public void setVersion(@Nonnull String version) {
            this.version = version;
        }
    }
}