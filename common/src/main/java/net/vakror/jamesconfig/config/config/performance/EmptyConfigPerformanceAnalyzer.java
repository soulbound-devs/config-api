package net.vakror.jamesconfig.config.config.performance;

import net.vakror.jamesconfig.config.config.Config;

public class EmptyConfigPerformanceAnalyzer implements IConfigPerformanceAnalyzer {

    public static final EmptyConfigPerformanceAnalyzer INSTANCE = new EmptyConfigPerformanceAnalyzer();

    @Override
    public String getPerformanceAnalysis(Config config) {
        return "";
    }
}
