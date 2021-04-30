package org.springultron.job;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * xxl-job 配置项
 *
 * @author brucewuu
 * @date 2021/4/7 下午6:37
 */
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    /**
     * 是否启用分布式调度任务，默认：开启
     */
    private boolean enabled = true;
    /**
     * 调度中心配置
     */
    private XxlJobAdminProps admin = new XxlJobAdminProps();
    /**
     * 任务执行器配置
     */
    private XxlJobExecutorProps executor = new XxlJobExecutorProps();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public XxlJobAdminProps getAdmin() {
        return admin;
    }

    public void setAdmin(XxlJobAdminProps admin) {
        this.admin = admin;
    }

    public XxlJobExecutorProps getExecutor() {
        return executor;
    }

    public void setExecutor(XxlJobExecutorProps executor) {
        this.executor = executor;
    }

    /**
     * 调度中心配置
     */
    public static class XxlJobAdminProps {
        /**
         * 调度中心部署跟地址 [选填]
         * 如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
         */
        private String address;
        /**
         * 执行器通讯TOKEN [选填]
         * 非空时启用
         */
        private String accessToken;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    /**
     * 任务执行器配置
     */
    public static class XxlJobExecutorProps {
        /**
         * 执行器AppName [选填]
         * 执行器心跳注册分组依据；为空则关闭自动注册
         */
        private String appName;
        /**
         * 执行器注册 [选填]
         * 优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        private String address;
        /**
         * 执行器IP [选填]
         * 默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于"执行器注册"和"调度中心请求并触发任务"；
         */
        private String ip;
        /**
         * 执行器端口号 [选填]
         * 小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
         */
        private int port = -1;
        /**
         * 执行器运行日志文件存储磁盘路径 [选填]
         * 需要对该路径拥有读写权限；为空则使用默认路径；
         */
        private String logPath;
        /**
         * 执行器日志文件保存天数 [选填]
         * 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        private int logRetentionDays = -1;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getLogPath() {
            return logPath;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public int getLogRetentionDays() {
            return logRetentionDays;
        }

        public void setLogRetentionDays(int logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }
    }
}
