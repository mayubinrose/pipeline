package com.ctg.pipeline.execute.basic.task;

import com.ctg.pipeline.execute.model.ExecutionStatus;
import com.ctg.pipeline.execute.model.Stage;
import com.google.common.collect.ImmutableMap;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
// @Builder //因为特殊的打包方式-pom.xml去掉了默认maven行为； 发现这个标签没办法编译，所以重写了个builder内部类
public final class TaskResult {
    /** A useful constant for a success result with no outputs. */
    public static final TaskResult SUCCEEDED = TaskResult.ofStatus(ExecutionStatus.SUCCEEDED);

    public static final TaskResult RUNNING = TaskResult.ofStatus(ExecutionStatus.RUNNING);

    @NonNull
    public final ExecutionStatus status;

    /**
     * Stage-scoped data.
     *
     * <p>
     * Data stored in the context will be available to other tasks within this stage, but not to tasks in other stages.
     */
    @Singular("context")
    public final Map<String, ?> context;

    /**
     * Pipeline-scoped data.
     *
     * <p>
     * Data stored in outputs will be available (via {@link Stage#getContext()} to tasks in later stages of the
     * pipeline.
     */
    @Singular("output")
    public final Map<String, ?> outputs;

    @Singular("logs")
    public final List<String> logs;

    public static TaskResult ofStatus(ExecutionStatus status) {
        return TaskResult.builder(status).build();
    }

    public static TaskResultBuilder builder(ExecutionStatus status) {
        return new TaskResultBuilder().status(status);
    }

    public static class TaskResultBuilder {
        private ExecutionStatus status;
        // private ImmutableMap<String, ?> context;

        private Map<String, ?> context;
        // private ImmutableMap<String, ?> outputs;

        private Map<String, ?> outputs;

        public List<String> logs;

        TaskResultBuilder() {}

        public TaskResult.TaskResultBuilder status(ExecutionStatus status) {
            init();
            this.status = status;
            return this;
        }

        public TaskResult.TaskResultBuilder context(Map<String, ?> context) {
            init();
            this.context = context;
            return this;
        }

        public TaskResult.TaskResultBuilder outputs(Map<String, ?> outputs) {
            init();
            this.outputs = outputs;
            return this;
        }

        public TaskResult build() {
            return new TaskResult(status, context, outputs,logs);
        }

        public TaskResult.TaskResultBuilder logs(List<String> logs){
            init();
            this.logs=logs;
            return this;
        }

        private void init() {
            if (null == this.context) {
                this.context = new HashMap<>();
            }
            if (null == this.outputs) {
                this.outputs = new HashMap<>();
            }
        }

    }
}
