package com.sunshine.generator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

/**
 * 生成model,api,di,repository,interactor,presenter,activity,fragment
 * author : sunny
 * email : zicai346@gmail.com
 * github : https://github.com/sunshinePrince
 * blog : http://mrjoker.wang
 */
public class GenerateWriter extends WriteCommandAction.Simple{


    private Project mProject;
    private PsiFile mFile;
    private PsiClass mClass;
    private PsiElementFactory mElementFactory;
    private GlobalSearchScope mScope;

    private String mLogicName;
    private String mMethodName;
    private List<String> mParams;
    private String mReturnType;





    protected GenerateWriter(Project project, PsiFile file,String logicName,String methodName, List<String> params,String returnType) {
        super(project);
        this.mProject = project;
        this.mFile = file;
        this.mLogicName = logicName;
        this.mMethodName = methodName;
        this.mParams = params;
        this.mReturnType = returnType;
        mElementFactory = JavaPsiFacade.getElementFactory(mProject);
        mScope = GlobalSearchScope.allScope(mProject);
    }

    private PsiDirectory subDir;

    @Override
    protected void run() throws Throwable {
        // TODO: 16/8/16 读入path
        String path = "src/com/sunshine/generate";
        String[] paths = path.split("/");
        PsiDirectory baseDir = PsiDirectoryFactory.getInstance(mProject).createDirectory(mProject.getBaseDir());
        for (String p:paths){
            subDir = baseDir.findSubdirectory(p);
            boolean isExit = subDir != null;
            if(!isExit){
                subDir = baseDir.createSubdirectory(p);
            }
            baseDir = subDir;
        }
//        PsiFile createFile = subDir.findFile("Test.java");
//        // TODO: 16/8/15 存在弹框确定重写
//        if(null != createFile){
//        }
        PsiClass psiClass = JavaDirectoryService.getInstance().createInterface(subDir,"I"+mLogicName+"Model");
        PsiJavaFile javaFile = (PsiJavaFile) psiClass.getContainingFile();
        javaFile.setPackageName("com.sunshine.generate.test");
        //导包
        // TODO: 16/8/16 类名需要读入
        addImportStatement("a.b.BodyResponse",javaFile);
//        for (PsiClass clz:searchs){
//            if("java.lang.Runnable".equals(clz.getQualifiedName())){
//                PsiJavaCodeReferenceElement ref = psiFactory.createClassReferenceElement(clz);
//                psiClass.getImplementsList().add(ref);
//                break;
//            }
//        }
        StringBuilder sb = new StringBuilder();
        sb.append("Observable<");
        sb.append(mReturnType);
        sb.append("> ");
        sb.append(mMethodName);
        sb.append("(");
        for (String param:mParams){
            sb.append("@ParamName(\"");
            sb.append(param);
            sb.append("\") String");
            sb.append(param);
            sb.append(",");
        }
        if(-1 != sb.indexOf(",")){
            sb.substring(0,sb.length()-1);
        }
        sb.append(");");
        PsiMethod method = mElementFactory.createMethodFromText(sb.toString(),psiClass);
        psiClass.add(method);

        //格式化代码
        CodeStyleManager.getInstance(mProject).reformat(psiClass);
        //在编辑器中打开
//        FileEditorManager.getInstance(mProject).openTextEditor(new OpenFileDescriptor(mProject,psiClass.getContainingFile().getVirtualFile()),true);
    }






    private void addImportStatement(String className,PsiJavaFile javaFile){
        PsiClass psiClass = JavaPsiFacade.getInstance(mProject).findClass(className,mScope);
        PsiImportStatement importStatement = mElementFactory.createImportStatement(psiClass);
        javaFile.getImportList().add(importStatement);
    }

















}
