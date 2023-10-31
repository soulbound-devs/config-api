package net.vakror.jamesconfig.config.config.performance;

import net.vakror.jamesconfig.config.config.Config;

public interface IConfigPerformanceAnalyzer {

    /**
     * @return the contents of the analysis file
     */
    String getPerformanceAnalysis(Config config);
}
