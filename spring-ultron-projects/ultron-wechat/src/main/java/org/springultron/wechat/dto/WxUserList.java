package org.springultron.wechat.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 公众号用户列表
 *
 * @author brucewuu
 * @date 2021/4/12 下午2:21
 */
@Schema(description = "公众号用户列表")
public class WxUserList {

    @Schema(description = "关注该公众账号的总用户数")
    private int total;

    @Schema(description = "拉取的OPENID个数，最大值为10000")
    private int count;

    @Schema(description = "列表数据，OPENID的列表")
    private Data data;

    @Schema(description = "拉取列表的最后一个用户的OPENID")
    @JsonAlias(value = "next_openid")
    private String nextOpenId;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }


    @Schema(description = "列表数据，OPENID的列表")
    public static class Data {

        @Schema(description = "用户openId列表")
        @JsonAlias(value = "openid")
        private List<String> openIds;

        public List<String> getOpenIds() {
            return openIds;
        }

        public void setOpenIds(List<String> openIds) {
            this.openIds = openIds;
        }
    }
}
