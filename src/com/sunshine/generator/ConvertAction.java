package com.sunshine.generator;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;

/**
 * author : sunny
 * email : zicai346@gmail.com
 * github : https://github.com/sunshinePrince
 * blog : http://mrjoker.wang
 */
public class ConvertAction extends BaseGenerateAction{

    public ConvertAction(){
        super(null);
    }


    public ConvertAction(CodeInsightActionHandler handler) {
        super(handler);
    }
}
