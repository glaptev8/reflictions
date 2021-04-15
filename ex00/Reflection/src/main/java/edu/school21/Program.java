package edu.school21;

import edu.school21.classes.car.Car;
import edu.school21.classes.user.User;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Program {

  public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
    Scanner scanner = new Scanner(System.in);
    Map<String, String> classes = new HashMap<>();
    for (Class clazz : ClassFinder.find("edu.school21.classes")) {
      classes.put(getNameAndPacket(clazz).getKey(), getNameAndPacket(clazz).getValue());
    }
    System.out.println("Classes:");
    classes.keySet().forEach(System.out::println);
    System.out.println("----------");
    System.out.println("Enter class name");
    String className = scanner.nextLine();
    System.out.println("----------");


    if (classes.containsKey(className)) {
      Class clazz = Class.forName(classes.get(className) + "." + className);
      printFields(clazz);
      printMethods(clazz);
      Object object = createObject(clazz, scanner);
      System.out.println("Object created: " + object);
      updateField(object, scanner);
      System.out.println("Object updated: " + object);
      invokeMethod(object, scanner);
    }
  }

  private static void invokeMethod(Object object, Scanner scanner) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    System.out.println("----------");
    System.out.println("Enter name of the method for call:");
    Class clazz = object.getClass();
    String methodName = scanner.nextLine();
    Method[] methods = clazz.getDeclaredMethods();
    Method method = null;
    for (Method method1: methods) {
      if (method1.getName().equals(methodName)) {
        method = method1;
        break;
      }
    }
    ArrayList<Object> arrayList = new ArrayList<>();
    for (int i = 0; i < method.getParameterCount(); i++) {
      System.out.println("Enter " + method.getParameterTypes()[i].getSimpleName() + " value:");
      arrayList.add(getNextArgument(method.getParameterTypes()[i], scanner));
    }
    Object invoke = method.invoke(object, arrayList.toArray(new Object[arrayList.size()]));
    System.out.println("Method returned:");
    System.out.println(invoke);
  }

    private static void updateField(Object object, Scanner scanner) throws NoSuchFieldException, IllegalAccessException {
    System.out.println("----------");
    System.out.println("Enter name of the field for changing:");
    String fieldName = scanner.nextLine();
    Class clazz = object.getClass();
    Field field = clazz.getDeclaredField(fieldName);
    System.out.println("Enter " + field.getType().getSimpleName() + " value:");
    Object argument = getNextArgument(field.getType(), scanner);
    field.setAccessible(true);
    field.set(object, argument);
  }

  private static Object getNextArgument(Class clazz, Scanner scanner) {
    switch (clazz.getSimpleName()) {
      case "int":
        return Integer.parseInt(scanner.nextLine());
      case "Integer":
        return Integer.parseInt(scanner.nextLine());
      case "long":
        return Long.parseLong(scanner.nextLine());
      case "Long":
        return Long.parseLong(scanner.nextLine());
      case "Double":
        return Double.parseDouble(scanner.nextLine());
      case "double":
        return Double.parseDouble(scanner.nextLine());
      case "Boolean":
        return Boolean.parseBoolean(scanner.nextLine());
      case "boolean":
        return Boolean.parseBoolean(scanner.nextLine());
      case "String":
        return scanner.nextLine();
      default:
        return null;
    }
  }

  private static Object createObject(Class clazz, Scanner scanner) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    System.out.println("----------");
    System.out.println("Let's create an object.");
    Field[] fields = clazz.getDeclaredFields();
    ArrayList<Class> paramsType = new ArrayList<>();
    ArrayList<Object> params = new ArrayList<>();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      System.out.println(field.getName() + ":");
      Map.Entry<String ,String> paramType = getNameAndPacket(field.getType());
      params.add(getNextArgument(field.getType(), scanner));
      if (field.getType().isPrimitive()) {
        paramsType.add(parseType(field.getType().getName()));
      } else {
        paramsType.add(Class.forName(paramType.getValue() + "." + paramType.getKey()));
      }
    }
    Constructor constructor = clazz.getConstructor(paramsType.toArray(new Class[paramsType.size()]));
    return constructor.newInstance(params.toArray(new Object[params.size()]));
  }

  static private void printMethods(Class clazz) {
    Method[] methods = clazz.getDeclaredMethods();
    System.out.println("methods :");
    if (methods.length != 0) {
      for (Method method: methods) {
        if (getNameAndPacket(method.getReturnType()).getKey().equals("void") || method.getName().equals("toString")) continue;
        System.out.print("    ");
        System.out.print(getNameAndPacket(method.getReturnType()).getKey() + " ");
        System.out.print(method.getName() + "(");
        for (int i = 0; i < method.getParameterTypes().length; i++) {
          Class clazzz = method.getParameterTypes()[i];
          System.out.print(getNameAndPacket(clazzz).getKey());
          if (i != method.getParameterCount() - 1) {
            System.out.print(", ");
          }
        }
        System.out.print(")");
        System.out.println();
      }
    }
  }

  static private void printFields(Class clazz) {
    Field[] fields = clazz.getDeclaredFields();
    System.out.println("fields :");
    if (fields.length != 0) {
      for (Field field: fields) {
        System.out.print("    ");
        System.out.print(getNameAndPacket(field.getType()).getKey() + " " + field.getName());
        System.out.println();
      }
    }
  }

  static private Map.Entry<String, String> getNameAndPacket(Class clazz) {
    return new AbstractMap.SimpleEntry<>(clazz.getSimpleName(), clazz.getName().replaceAll("." + clazz.getSimpleName(), ""));
  }

  public static Class<?> parseType(final String className) {
    switch (className) {
      case "boolean":
        return boolean.class;
      case "byte":
        return byte.class;
      case "short":
        return short.class;
      case "int":
        return int.class;
      case "long":
        return long.class;
      case "float":
        return float.class;
      case "double":
        return double.class;
      case "char":
        return char.class;
      case "void":
        return void.class;
      default:
        return null;
    }
  }

  public static class ClassFinder {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    /**
     * Возвращает список классов в пакете
     */
    public static List<Class<?>> find(String scannedPackage) {
      String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
      URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
      if (scannedUrl == null) {
        throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
      }
      File scannedDir = new File(scannedUrl.getFile());
      List<Class<?>> classes = new ArrayList<>();
      for (File file : scannedDir.listFiles()) {
        classes.addAll(find(file, scannedPackage));
      }
      return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
      List<Class<?>> classes = new ArrayList<>();
      String resource = scannedPackage + PKG_SEPARATOR + file.getName();
      if (file.isDirectory()) {
        for (File child : file.listFiles()) {
          classes.addAll(find(child, resource));
        }
      } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
        int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
        String className = resource.substring(0, endIndex);
        try {
          classes.add(Class.forName(className));
        } catch (ClassNotFoundException ignore) {
        }
      }
      return classes;
    }
  }
}
