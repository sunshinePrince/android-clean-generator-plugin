package com.sunshine.generator;

/**
 * rest api 与 model 自动生成
 * Created by sunny on 16/4/27.
 * email : zicai346@gmail.com
 * github : https://github.com/sunshinePrince
 * blog : http://mrjoker.wang
 */
public class Generate {


//
//
//	/**
//	 * 扫描目标文件夹路径
//	 */
//	private final String mTargetFileDirPath;
//
//
//	/**
//	 * model所在包名
//	 */
//	private final String mModelPackage;
//
//
//	/**
//	 * api所在包名
//	 */
//	private final String mAPIPackage;
//
//
//	private String activity_end = "URLS.java";
//
//
//	private String annotation_default = "value";
//
//
//	/**
//	 * 扫描时存放创建model相关
//	 */
//	private Map<String, List<MethodSpec>> mModelCache = new HashMap<>();
//
//
//	/**
//	 * 扫描时存放创建api相关
//	 */
//	private List<MethodSpec> mAPICache = new ArrayList<>();
//
//
//	/**
//	 * 模块
//	 */
//	private String moduleName;
//
//
//	public Generate(String target, String modelPackage, String apiPackage) {
//		mTargetFileDirPath = target;
//		mModelPackage = modelPackage.replaceAll("/", ".");
//		mAPIPackage = apiPackage.replaceAll("/", ".");
//	}
//
//
//	/**
//	 * 生成java file
//	 */
//	public void generate() {
//        		System.out.println("start to create model and api");
//		//扫描目标文件夹下所有文件,将其中带有URL注解的属性取出,并生成对应api,model
//		File scanDir = new File(mTargetFileDirPath);
//		if (!scanDir.isDirectory()) {
//			throw new RuntimeException("target file path not a directory");
//		}
//		File[] files = scanDir.listFiles();
//		//解析指定文件夹下的java源文件
//		parseJavaFile(files);
//	}
//
//
//	/**
//	 * 解析java源文件
//	 *
//	 * @param files 文件集合
//	 */
//	private void parseJavaFile(File[] files) {
//		for (File file : files) {
//			if (file.isDirectory()) {
//				parseJavaFile(file.listFiles());
//			} else {
//				mModelCache.clear();
//				mAPICache.clear();
//				try {
//					if (file.getName().equals(activity_end)) {//解析这个java文件
//						CompilationUnit cu = JavaParser.parse(file);
//						String packName = cu.getPackage().getName().toString();
//						Class clz = Class.forName(packName + ".URLS");
//						String module = file.getPath().substring(mTargetFileDirPath.length());
//						module = module.startsWith("/") ? module.substring(1) : module;
//						String[] splits = module.split("/");
//						StringBuilder sb = new StringBuilder();
//						for (int i = 0; i < splits.length - 1; i++) {
//							if (0 != i) {
//								sb.append(".");
//							}
//							sb.append(splits[i]);
//						}
//						moduleName = sb.toString();
//						parseFields(clz);
//					}
//					//生成java源文件
//					create();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (ParseException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//
//	/**
//	 * 解析所有属性
//	 *
//	 * @param clz
//	 */
//	private void parseFields(Class clz) {
//		Field[] fields = clz.getDeclaredFields();
//		for (Field field : fields) {
//			URL url = field.getAnnotation(URL.class);
//			if (null != url) {
//				parseField(clz, field);
//			}
//		}
//	}
//
//
//	/**
//	 * 解析属性
//	 *
//	 * @param clz
//	 * @param field
//	 * @throws IOException
//	 */
//	private void parseField(Class clz, Field field) {
//		URL url = field.getAnnotation(URL.class);
//		if (null != url) {
//			//获取配置
//			String module = url.logicName();
//			String[] params = url.params();
//			Class<?> response = url.response();
//			long timeout = url.timeout();
//			String methodName = url.methodName();
//			NetType netType = url.netType();
//			int retryNumber = url.retryNumber();
//
//			ParameterizedTypeName responseType = ParameterizedTypeName.get(
//					ClassName.get(BodyResponse.class), WildcardTypeName.get(response));
//			ParameterizedTypeName returnType = ParameterizedTypeName.get(
//					ClassName.get(Observable.class), responseType);
//			if (methodName.trim().equals("")) {
//				String[] splits = field.getName().split("_");
//				methodName = splits[splits.length - 1].toLowerCase();
//			}
//			cacheModel(field, module, params, methodName, returnType);
//			cacheApi(clz, field, params, methodName, netType, returnType);
//		}
//	}
//
//
//	/**
//	 * 缓存model
//	 *
//	 * @param field
//	 * @param module
//	 * @param params
//	 * @param methodName
//	 * @param returnType
//	 */
//	private void cacheModel(Field field, String module, String[] params, String methodName, ParameterizedTypeName returnType) {
//		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName).addModifiers(
//				Modifier.PUBLIC, Modifier.ABSTRACT).returns(returnType);
//		for (String param : params) {
//			AnnotationSpec as = AnnotationSpec.builder(ParamName.class).addMember(
//					annotation_default, "\"" + param + "\"").build();
//			ParameterSpec ps = ParameterSpec.builder(String.class, param).addAnnotation(as).build();
//			methodBuilder.addParameter(ps);
//		}
//		MethodSpec methodSpec = methodBuilder.build();
//		String logicName = module.length() > 1 ? module.substring(0,
//				1).toUpperCase() + module.substring(1) : module.toUpperCase();
//		if (mModelCache.containsKey(logicName)) {
//			mModelCache.get(logicName).add(methodSpec);
//		} else {
//			List<MethodSpec> list = new ArrayList<>();
//			list.add(methodSpec);
//			mModelCache.put(logicName, list);
//		}
//	}
//
//
//	private void cacheApi(Class clz, Field field, String[] params, String methodName, NetType netType, ParameterizedTypeName returnType) {
////		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName).addModifiers(
////				Modifier.PUBLIC, Modifier.ABSTRACT);
//		MethodSpec.Builder methodBuilder = null;
//		AnnotationSpec asForm = AnnotationSpec.builder(FormUrlEncoded.class).build();
//		AnnotationSpec.Builder typeBuilder;
//		ParameterizedTypeName ptn = ParameterizedTypeName.get(ClassName.get(Map.class),
//				WildcardTypeName.get(String.class), WildcardTypeName.get(String.class));
//		ParameterSpec.Builder paramsBuilder = ParameterSpec.builder(ptn, "params");
//		if (NetType.POST == netType) {
//			typeBuilder = AnnotationSpec.builder(POST.class);
//			methodBuilder.addAnnotation(asForm);
//			methodBuilder.addParameter(paramsBuilder.addAnnotation(
//					AnnotationSpec.builder(FieldMap.class).build()).build());
//		} else {
//			typeBuilder = AnnotationSpec.builder(GET.class);
//			methodBuilder.addParameter(paramsBuilder.addAnnotation(
//					AnnotationSpec.builder(QueryMap.class).build()).build());
//		}
//		AnnotationSpec asType = typeBuilder.addMember(annotation_default, "$T.$L", clz,
//				field.getName()).build();
//		methodBuilder.addAnnotation(asType);
//		MethodSpec ms = methodBuilder.returns(returnType).build();
//		mAPICache.add(ms);
//	}
//
//	private void create() {
//		crateModels();
//		createAPIs();
//	}
//
//
//	/**
//	 * 生成model
//	 */
//	private void crateModels() {
//		DIGenerator diGenerator = new DIGenerator(
//				System.getProperty("user.dir") + "/presentation/src/main/java");
//		String output = System.getProperty("user.dir") + "/data/src/main/java";
//		for (String logicName : mModelCache.keySet()) {
//			String modelName = "I" + logicName + "Model";
//			String apiClz = moduleName.substring(0, 1).toUpperCase() + moduleName.substring(
//					1) + "API";
//			AnnotationSpec as = AnnotationSpec.builder(API.class).addMember(annotation_default,
//					"$T.class", ClassName.get(mAPIPackage, apiClz)).build();
////			TypeSpec ts = TypeSpec.interfaceBuilder(modelName).addModifiers(
////					Modifier.PUBLIC).addAnnotation(as).addMethods(
////					mModelCache.get(logicName)).build();
//			TypeSpec ts = null;
//			JavaFile javaFile = JavaFile.builder(mModelPackage + "." + moduleName, ts).build();
//			try {
//				javaFile.writeTo(new File(output));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			diGenerator.generate(moduleName, logicName);
//		}
//	}
//
//
//	/**
//	 * 生成api
//	 */
//	private void createAPIs() {
//		String output = System.getProperty("user.dir") + "/data/src/main/java";
//		String className = moduleName.substring(0, 1).toUpperCase() + moduleName.substring(1) + "API";
//		TypeSpec ts = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC).addMethods(
//				mAPICache).build();
//		JavaFile javaFile = JavaFile.builder(mAPIPackage, ts).build();
//		try {
//			javaFile.writeTo(new File(output));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
