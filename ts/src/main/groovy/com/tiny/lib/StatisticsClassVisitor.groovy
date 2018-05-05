package com.tiny.lib


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 类描述
 * 创建者:tiny
 * 日期:18/3/12
 */

public class StatisticsClassVisitor extends ClassVisitor {

    private String mClassName;

    private String superName;

    private String[] mInterfaces;

    /**
     * 是否满足条件，满足条件的类才会修改中指定的方法
     */
    private boolean isMeetClassCondition = false

    public StatisticsClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor)
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {

//        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
//        MethodVisitor adapter = null;
//        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {
//
//
//            private boolean isInject() {
//                System.out.println("-----" + superName);
//                System.out.println("=====" + className);
//                System.out.println("=======" + (interfaces == null ? "" : interfaces.toString()));
//                return superName.contains("AppCompatActivity") || superName.contains("Fragment");
//            }
//
//            @Override
//            public void visitCode() {
//                super.visitCode();
//
//            }
//
//            @Override
//            public org.objectweb.asm.AnnotationVisitor visitAnnotation(String d, boolean visible) {
//                return super.visitAnnotation(d, visible);
//            }
//
//            @Override
//            public void visitFieldInsn(int opcode, String owner, String n, String de) {
//                super.visitFieldInsn(opcode, owner, n, de);
//            }
//
//
//            @Override
//            protected void onMethodEnter() {
//                //super.onMethodEnter();
//                if (isInject()) {
//                    if (superName.contains("AppCompatActivity")) {
//                        if ("onResume".equals(name)) {
//                            mv.visitVarInsn(ALOAD, 0);
//                            mv.visitMethodInsn(INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils",
//                                    "onActivityResume", "(Landroid/app/Activity;)V", false);
//                        }
//                    }
//                    if (superName.equals("android/support/v4/app/Fragment")) {
//                        if ("onResume".equals(name)) {
//                            mv.visitVarInsn(ALOAD, 0);
//                            mv.visitMethodInsn(INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils",
//                                    "onFragmentResume", "(Landroid/support/v4/app/Fragment;)V", false);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            protected void onMethodExit(int i) {
//                super.onMethodExit(i);
//            }
//
//        };
//        adapter = new AutoMethodVisitor(methodVisitor, access, name, desc);
//        return adapter


        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        MethodVisitor adapter = null

        if ((isMeetClassCondition && ChoiceUtil.isMatchingMethod(name, desc))) {
            try {
                adapter = ChoiceUtil.getMethodVisitor(mInterfaces, mClassName, superName, methodVisitor, access, name, desc)
            } catch (Exception e) {
                e.printStackTrace()
                adapter = null
            }
        }
        if (adapter != null) {
            return adapter
        }
        return methodVisitor
    }

    @Override
    public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
        super.visit(i, i1, s, s1, s2, strings);
        isMeetClassCondition = ChoiceUtil.isMatchingClass(s, s2, strings)
        this.mClassName = s
        this.superName = s2
        this.mInterfaces = strings
    }
}
