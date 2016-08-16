package com.sunshine.generator;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import com.sunshine.generator.common.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * author : sunny
 * email : zicai346@gmail.com
 * github : https://github.com/sunshinePrince
 * blog : http://mrjoker.wang
 */
public class GenerateAction extends BaseGenerateAction {


    private PsiDirectory subDir;
    private JLabel nameLabel;
    private JLabel packageLabel;
    private JLabel checkLabel;
    private JLabel weiLabel;


    public GenerateAction(){
        super(null);
    }

    public GenerateAction(CodeInsightActionHandler handler) {
        super(handler);
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor,project);
        //offset
        int offset = editor.getCaretModel().getOffset();
        //这一行第几列
        int column = editor.getCaretModel().getLogicalPosition().column;
        //这一行开始offset
        offset -= column;
        PsiElement element = file.findElementAt(offset);
        while (element instanceof PsiWhiteSpace){
            offset++;
            element = file.findElementAt(offset);
        }
        element = element.getParent();
        file.getVirtualFile().getClass();
        String logicName = "";
        String methodName = "";
        String netType = "";
        String returnType = "";
        List<String> params = new ArrayList<>();
        if(element instanceof PsiAnnotation){
            PsiAnnotation psiAnnotation = (PsiAnnotation) element;
            PsiNameValuePair[] keys = psiAnnotation.getParameterList().getAttributes();
            for (PsiNameValuePair pair:keys){
                String name = pair.getName();
                PsiAnnotationMemberValue value = pair.getValue();
                switch (name){
                    case "logicName":
                        logicName = Utils.getStringFromPsiAnnotationMemberValue(value);
                        break;
                    case "methodName":
                        methodName = Utils.getStringFromPsiAnnotationMemberValue(value);
                        break;
                    case "params":
                        if(value instanceof PsiLiteralExpression){
                            String param = Utils.getStringFromPsiAnnotationMemberValue(value);
                        }else if(value instanceof PsiArrayInitializerMemberValue){
                            PsiArrayInitializerMemberValue array = (PsiArrayInitializerMemberValue) value;
                            for (PsiAnnotationMemberValue v:array.getInitializers()){
                                String param = Utils.getStringFromPsiAnnotationMemberValue(v);
                            }
                        }
                        break;
                    case "response":
                        if(value instanceof PsiClassObjectAccessExpression){
                            PsiClassObjectAccessExpression obj = (PsiClassObjectAccessExpression) value;
                            System.out.println(obj.getType());
                            System.out.println(obj.getText());
                            returnType = obj.getText();
                        }
                        break;
                    case "netType":
                        if(value instanceof PsiReferenceExpression){
                            PsiReferenceExpression expression = (PsiReferenceExpression) value;
                            netType = expression.getText();
                            System.out.println(expression.resolve().getText());
                        }
                        System.out.println(value);
                        break;
                }
            }
        }else{
            // TODO: 16/8/16 没有找到url注解,弹出错误
            System.out.println("没有定位到url");
            return;
        }
        //generate
        GenerateWriter writer = new GenerateWriter(project,file,logicName,methodName,params,returnType);
        writer.execute();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
