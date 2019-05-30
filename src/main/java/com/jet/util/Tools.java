package com.jet.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Tools {
	/**
	 * 判断目标值是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmptyOrNull(Object obj) {
		boolean flag = false;
		if (obj == null) {
			flag = true;
		} else {
			if (("").equals(obj.toString().trim())) {
				flag = true;
			}
		}
		return flag;
	}


	public static String getTheLengthNum(int num, int length, int pos) {
		String theNum = "";

		return theNum;
	}

	public static List<String> getClassInPackage(String pkgName) {
		String appBasePath = Tools.class.getProtectionDomain().getCodeSource().getLocation().toString();
		appBasePath = appBasePath.replace("file:/", "");

		List<String> ret = new ArrayList<String>();
		String rPath = pkgName.replace('.', '/') + "/";
		try {

			File appPath = new File(appBasePath);
			if (appPath.isDirectory()) {
				File dir = new File(appPath, rPath);
				if (dir.exists()) {
					for (File file : dir.listFiles()) {
						if (file.isFile()) {
							String clsName = file.getName();
							clsName = pkgName + "." + clsName.substring(0, clsName.length() - 6);
							ret.add(clsName);
						} else if (file.isDirectory()) {
							String tmpPkgName = pkgName + "." + file.getName();
							ret.addAll(getClassInPackage(tmpPkgName));
						}
					}
				}
			} else {
				FileInputStream fis = new FileInputStream(appPath);
				JarInputStream jis = new JarInputStream(fis, false);
				JarEntry e = null;
				while ((e = jis.getNextJarEntry()) != null) {
					String eName = e.getName();
					if (eName.startsWith(rPath) && !eName.endsWith("/")) {
						ret.add(eName.replace('/', '.').substring(0, eName.length() - 6));
					}
					jis.closeEntry();
				}
				jis.close();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ret;
	}

	/**
	 * 判断一个类，是否实现指定名称的接口
	 * 
	 * @param c
	 * @param interfaceName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isInterface(Class c, String interfaceName) {
		Class[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(interfaceName)) {
				return true;
			} else {
				Class[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(interfaceName)) {
						return true;
					} else if (isInterface(face1[x], interfaceName)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), interfaceName);
		}
		return false;
	}
}
