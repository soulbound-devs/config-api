package net.vakror.jamesconfig.config.config.performance;

import com.google.common.base.Stopwatch;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.registry.multi_object.MultiObjectRegistryConfigImpl;
import net.vakror.jamesconfig.config.config.registry.single_object.SingleObjectRegistryConfigImpl;
import net.vakror.jamesconfig.config.config.setting.SettingConfigImpl;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class DefaultConfigPerformanceAnalyzer implements IConfigPerformanceAnalyzer {

    DecimalFormat format = new DecimalFormat("0.##");

    public static final DefaultConfigPerformanceAnalyzer INSTANCE = new DefaultConfigPerformanceAnalyzer();

    @Override
    public String getPerformanceAnalysis(Config config) {
        if (config instanceof MultiObjectRegistryConfigImpl multiObjectRegistryConfig) {
            return getMultiObjectRegistryConfigAnalysis(multiObjectRegistryConfig);
        } else if (config instanceof SingleObjectRegistryConfigImpl<?> singleObjectRegistryConfig) {
            return getSingleObjectRegistryConfigAnalysis(singleObjectRegistryConfig);
        } else if (config instanceof SettingConfigImpl settingConfig) {
            return getSettingConfigAnalysis(settingConfig);
        }
        return simpleAnalysis(config);
    }

    private String getMultiObjectRegistryConfigAnalysis(MultiObjectRegistryConfigImpl multiObjectRegistryConfig) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        multiObjectRegistryConfig.readConfig(false);
        stopwatch.stop();
        StringBuilder builder = new StringBuilder();
        builder.append("total load time = ").append(stopwatch).append("\n");
        builder.append("total parse time = ").append(multiObjectRegistryConfig.loadTime.toString()).append("\n");
        builder.append("parse time:").append("\n");
        long totalObjectLoadTime = multiObjectRegistryConfig.parseTime.values().stream().mapToLong(stopwatch1 -> stopwatch1.elapsed(TimeUnit.NANOSECONDS)).sum();
        multiObjectRegistryConfig.parseTime.forEach((key, time) -> {
            String percent = format.format((((double) time.elapsed(TimeUnit.NANOSECONDS)) / (double) totalObjectLoadTime) * 100);
            builder.append("    ").append(key).append(" = ").append(time).append(" (").append(percent).append("%)").append("\n");
        });
        return builder.toString();
    }

    private String getSingleObjectRegistryConfigAnalysis(SingleObjectRegistryConfigImpl<?> singleObjectRegistryConfig) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        singleObjectRegistryConfig.readConfig(false);
        stopwatch.stop();
        StringBuilder builder = new StringBuilder();
        builder.append("total load time = ").append(stopwatch).append("\n");
        builder.append("total parse time = ").append(singleObjectRegistryConfig.loadTime.toString()).append("\n");
        builder.append("parse time:").append("\n");
        long totalObjectLoadTime = singleObjectRegistryConfig.parseTime.values().stream().mapToLong(stopwatch1 -> stopwatch1.elapsed(TimeUnit.NANOSECONDS)).sum();
        singleObjectRegistryConfig.parseTime.forEach((key, time) -> {
            String percent = format.format((((double) time.elapsed(TimeUnit.NANOSECONDS)) / (double) totalObjectLoadTime) * 100);
            builder.append("    ").append(key).append(" = ").append(time).append(" (").append(percent).append("%)").append("\n");
        });
        return builder.toString();
    }

    private String getSettingConfigAnalysis(SettingConfigImpl settingConfig) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        settingConfig.readConfig(false);
        stopwatch.stop();
        StringBuilder builder = new StringBuilder();
        builder.append("total load time = ").append(stopwatch).append("\n");
        builder.append("load parse time = ").append(settingConfig.loadTime.toString()).append("\n");
        builder.append("parse time:").append("\n");
        long totalObjectLoadTime = settingConfig.parseTime.values().stream().mapToLong(stopwatch1 -> stopwatch1.elapsed(TimeUnit.NANOSECONDS)).sum();
        settingConfig.parseTime.forEach((key, time) -> {
            String percent = format.format((((double) time.elapsed(TimeUnit.NANOSECONDS)) / (double) totalObjectLoadTime) * 100);
            builder.append("    ").append(key).append(" = ").append(time).append(" (").append(percent).append("%)").append("\n");
        });
        return builder.toString();
    }

    private String simpleAnalysis(Config config) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        config.readConfig(false);
        stopwatch.stop();
        return "load time = " + stopwatch;
    }
}
