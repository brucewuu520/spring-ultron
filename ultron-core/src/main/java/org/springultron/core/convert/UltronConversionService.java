package org.springultron.core.convert;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * 类型 转换 服务，添加 IEnum 转换
 *
 * @Auther: brucewuu
 * @Date: 2019-08-12 11:38
 * @Description:
 */
public class UltronConversionService extends ApplicationConversionService {

    @Nullable
    private static volatile UltronConversionService SHARED_INSTANCE;

    public UltronConversionService() {
        this(null);
    }

    public UltronConversionService(@Nullable StringValueResolver embeddedValueResolver) {
        super(embeddedValueResolver);
        super.addConverter(new EnumToStringConverter());
        super.addConverter(new StringToEnumConverter());
    }

    public static UltronConversionService getInstance() {
        UltronConversionService sharedInstance = UltronConversionService.SHARED_INSTANCE;
        if (null == sharedInstance) {
            synchronized (UltronConversionService.class) {
                sharedInstance = UltronConversionService.SHARED_INSTANCE;
                if (null == sharedInstance) {
                    sharedInstance = new UltronConversionService();
                    UltronConversionService.SHARED_INSTANCE = sharedInstance;
                }
            }
        }
        return sharedInstance;
    }
}
