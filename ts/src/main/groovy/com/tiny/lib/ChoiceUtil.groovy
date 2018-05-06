package com.tiny.lib

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class ChoiceUtil {
    /**
     * 获取自动埋点的方法修改器
     *
     * @param interfaces 实现的接口名
     * @param className 类名
     * @param methodVisitor 需要修改的方法
     * @param name 方法名
     * @param desc 参数描述符
     */
    static MethodVisitor getMethodVisitor(String[] interfaces, String className, String superName,
                                          MethodVisitor methodVisitor, int access, String name, String desc) {
        MethodVisitor adapter = null

        if (name == "onClick" && isMatchingInterfaces(interfaces, 'android/view/View$OnClickListener')) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodEnter() {
                    super.onMethodEnter()
                    // ALOAD 25
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    //向栈中压入类名称
                    methodVisitor.visitLdcInsn(className)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils", "onClick", "(Landroid/view/View;Ljava/lang/String;)V", false)
                }
            }
        } else if (name == "onResume" && superName.equalsIgnoreCase("android/support/v4/app/Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils", "onFragmentResume", "(Landroid/support/v4/app/Fragment;)V", false)
                }
            }
        } else if (name == "onResume" && superName.equalsIgnoreCase("android/support/v7/app/AppCompatActivity")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils", "onActivityResume", "(Landroid/app/Activity;)V", false)
                }
            }
        } else if (name == "setUserVisibleHint" && className.contains("Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD 25
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    // ILOAD 21
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils", "setFragmentUserVisibleHint", "(Landroid/support/v4/app/Fragment;Z)V", false)
                }
            }
        } else if (name == "onHiddenChanged" && className.contains("Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD 25
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    // ILOAD 21
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "tiny/tinystatistics/utils/StatisticsUtils", "onFragmentHiddenChanged", "(Landroid/support/v4/app/Fragment;Z)V", false)
                }
            }
        }

        return adapter
    }

    /**
     * 接口名是否匹配
     *
     * @param interfaces 类的实现接口
     * @param interfaceName 需要匹配的接口名
     */
    private static boolean isMatchingInterfaces(String[] interfaces, String interfaceName) {
        boolean isMatch = false
        // 是否满足实现的接口
        interfaces.each {
            String inteface ->
                if (inteface == interfaceName) {
                    isMatch = true
                }
        }
        return isMatch
    }

    /**
     * 类是否满足匹配条件，满足的才会允许修改其中的方法
     * 1、实现android/view/View$OnClickListener接口的类
     *
     * @param className 类名
     * @param desc 类的实现接口
     */
    static boolean isMatchingClass(String className, String superName, String[] interfaces) {
        boolean isMeetClassCondition = false
        System.out.println("className is " + className + " superName is " + superName)
        //剔除掉以android开头的类，即系统类，以避免出现不可预测的bug
        if (className.startsWith('android')) {
            return isMeetClassCondition
        }
        // 是否满足实现的接口
        isMeetClassCondition = isMatchingInterfaces(interfaces, 'android/view/View$OnClickListener')
        if (superName.equalsIgnoreCase("android/support/v4/app/Fragment") ||
                superName.equalsIgnoreCase("android/support/v7/app/AppCompatActivity")) {
            isMeetClassCondition = true
        }
        return isMeetClassCondition
    }

    /**
     * 方法是否匹配到，根据方法名和参数的描述符来确定一个方法是否需要修改的初步条件，
     * 具体要怎么修改要在{@link ChoiceUtil#getMethodVisitor}中确定：
     * 1、View的onClick(View v)方法
     * 2、Fragment的onResume()方法
     * 3、Fragment的onPause()方法
     * 4、Fragment的setUserVisibleHint(boolean b)方法
     * 5、Fragment的onHiddenChanged(boolean b)方法
     * 6、在app的module中手动设置的监听条件：指定方法或注解方法
     * 可以扩展自己想监听的方法
     *
     * @param name 方法名
     * @param desc 参数的方法的描述符
     */
    static boolean isMatchingMethod(String name, String desc) {
        if ((name == 'onClick' && desc == '(Landroid/view/View;)V')
                || (name == 'onResume' && desc == '()V')
                || (name == 'onPause' && desc == '()V')
                || (name == 'setUserVisibleHint' && desc == '(Z)V')
                || (name == 'onHiddenChanged' && desc == '(Z)V')
        ) {
            return true
        } else {
            return false
        }
    }
}