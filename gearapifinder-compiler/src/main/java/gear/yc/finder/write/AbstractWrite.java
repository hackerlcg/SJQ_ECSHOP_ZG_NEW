package gear.yc.finder.write;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.util.Elements;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/1 15:46.
 */

public abstract class AbstractWrite {

    protected Elements mElementUtils;//元素相关
    protected boolean isGenerate=false;
    protected TypeSpec typeClass;
}
