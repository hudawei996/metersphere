package io.metersphere.performance.service;

import io.metersphere.base.domain.LoadTestReport;
import io.metersphere.base.domain.LoadTestReportWithBLOBs;
import io.metersphere.base.mapper.LoadTestReportMapper;
import io.metersphere.commons.constants.PerformanceTestStatus;
import io.metersphere.commons.consumer.LoadTestFinishEvent;
import io.metersphere.commons.utils.CommonBeanFactory;
import io.metersphere.commons.utils.LogUtil;
import io.metersphere.service.QuotaService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lyh
 */

@Component
public class LoadTestVumEvent implements LoadTestFinishEvent {
    @Resource
    private LoadTestReportMapper loadTestReportMapper;

    private void handleVum(LoadTestReport report) {
        if (report == null) {
            return;
        }

        LoadTestReportWithBLOBs testReport = loadTestReportMapper.selectByPrimaryKey(report.getId());
        if (testReport == null) {
            return;
        }

        QuotaService quotaService = CommonBeanFactory.getBean(QuotaService.class);
        if (quotaService != null) {
            quotaService.reduceVumUsed(testReport);
        } else {
            LogUtil.error("handle vum event get quota service bean is null. load test report id: " + report.getId());
        }
    }

    @Override
    public void execute(LoadTestReport report) {
        if (PerformanceTestStatus.Error.name().equals(report.getStatus())) {
            // 失败后回退vum数量
            this.handleVum(report);
        }
    }
}
