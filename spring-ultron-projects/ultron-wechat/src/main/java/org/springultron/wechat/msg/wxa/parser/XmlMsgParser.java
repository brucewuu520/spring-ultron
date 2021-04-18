package org.springultron.wechat.msg.wxa.parser;

import org.springultron.core.utils.TypeConverterUtils;
import org.springultron.core.utils.ReflectUtils;
import org.springultron.core.utils.StringUtils;
import org.springultron.wechat.msg.XmlHelper;
import org.springultron.wechat.msg.wxa.MsgModel;
import org.springultron.wechat.msg.wxa.WxaMsg;

import java.lang.reflect.Field;

/**
 * xml消息解析器
 *
 * @author brucewuu
 * @date 2021/4/16 下午5:57
 */
public class XmlMsgParser implements IMsgParser {

    /**
     * xml消息解析
     *
     * @param msgStr json消息
     * @return WxaMsg
     */
    @Override
    public WxaMsg parser(String msgStr) {
        XmlHelper xmlHelper = XmlHelper.of(msgStr);
        MsgModel msgModel = toMsgModel(xmlHelper);
        return parseMsgModel(msgModel);
    }

    private static MsgModel toMsgModel(XmlHelper xmlHelper) {
        MsgModel msgModel = new MsgModel();
        try {
            Field[] fields = MsgModel.class.getDeclaredFields();
            for (Field field : fields) {
                XPath xPathAnn = field.getAnnotation(XPath.class);
                if (xPathAnn == null) {
                    continue;
                }
                String xPath = xPathAnn.value();
                String valueStr = xmlHelper.getString(xPath);
                if (StringUtils.isEmpty(valueStr)) {
                    continue;
                }
                Class<?> type = field.getType();
                ReflectUtils.makeAccessible(field);
                field.set(msgModel, TypeConverterUtils.convertIfNecessary(valueStr, type, field));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return msgModel;
    }
}
