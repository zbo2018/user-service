package com.weaver.rpa.tender.search.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.sim.dtu.server.tools
 * create class FileUtil.java
 * @author zhang bo
 * @description 
 *	该类用于操作文件和文件夹
 * @createTime 2014年9月28日  下午6:51:36
 */
public class FileUtil {

	/**
	 * 复制单个文件
	 * @param sourceFile  准备复制的文件源
	 * @param destFile 拷贝到新绝对路径带文件名
	 * @return
     * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static void copy(String sourceFile, String destFile) throws IOException {
			
		File source = new File(sourceFile);
		if (source.exists()) {
			FileChannel inputChannel = null;
			FileChannel outputChannel = null;
			try {
				File dest = new File(destFile);
				inputChannel = new FileInputStream(source).getChannel();
				outputChannel = new FileOutputStream(dest).getChannel();
				outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			} finally {
				inputChannel.close();
				outputChannel.close();
			}
		}
	}
	
	/**
	 * 复制整个文件夹的内容
	 * @param oldPath  准备拷贝的目录
	 * @param newPath  指定绝对路径的新目录
	 * @return
	 */
	@SuppressWarnings("resource")
	public static void copyDir(String oldPath, String newPath) {
		try {
			/**如果文件夹不存在 则建立新文件**/
			new File(newPath).mkdirs(); 
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					
					FileChannel inputChannel = null;
					FileChannel outputChannel = null;
					try {
						File dest = new File(newPath + "/" + (temp.getName()).toString());
						inputChannel = new FileInputStream(temp).getChannel();
						outputChannel = new FileOutputStream(dest).getChannel();
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
					} finally {
						inputChannel.close();
						outputChannel.close();
					}
				}
				/**如果是子文件**/
				if (temp.isDirectory()) {
					copyDir(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件
	 * @param oldPath
	 * @param newPath
	 * @return
	 * @throws IOException 
	 */
	public static void moveFile(String oldPath, String newPath) throws IOException {
		copy(oldPath, newPath);
		try {
			deleteFile(oldPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动目录
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyDir(oldPath, newPath);
		try {
			deleteFolder(oldPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
     * 将源文件的数据写入到目标文件中， 
     * 不会检查源文件是否存在， 
     * 若目标文件存在则直接写入， 
     * 否则创建目标文件后再进行写入。 
     * @param srcPath 
     * @param desPath 
     */  
    private static void copyFile(String srcPath,String desPath){  
        try {  
			createFile(desPath);
            FileInputStream in = new FileInputStream(srcPath);
            FileOutputStream out = new FileOutputStream(desPath);  
            byte[] bt = new byte[1024];  
            int count;  
            while ((count = in.read(bt)) > 0) {
                out.write(bt, 0, count);
            }
            in.close();  
            out.close();   
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } catch (Exception e) {
			e.printStackTrace();
		}        
    }  
      
    /** 
     * 复制文件，若文件存在则替换该文件。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception 
     */  
    public static void copyAndReplaceFile(String srcPath,String desPath) throws Exception{  
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        File srcFile = new File(srcPath);  
        if(!srcFile.isFile()){  
            throw new Exception("source file not found!");  
        }  
        copyFile(srcPath,desPath);  
    }  
      
    /** 
     * 复制文件，若文件已存在则不进行替换。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception 
     */  
    public static void copyAndNotReplaceFile(String srcPath,String desPath) throws Exception{  
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        File srcFile = new File(srcPath);  
        File desFile = new File(desPath);  
        if(!srcFile.isFile()){  
            throw new Exception("source file not found!");  
        }  
        if(desFile.isFile()){  
            return;  
        }  
        copyFile(srcPath,desPath);  
    }  
      
    /** 
     * 移动文件，若文件存在则替换该文件。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception 
     */  
    public static void moveAndReplaceFile(String srcPath,String desPath) throws Exception{  
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        copyAndReplaceFile(srcPath,desPath);  
        deleteFile(srcPath);  
    }  
      
    /** 
     * 移动文件，若文件存在则不进行替换。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception 
     */  
    public static void moveAndNotReplaceFile(String srcPath,String desPath) throws Exception{  
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        copyAndNotReplaceFile(srcPath,desPath);  
        deleteFile(srcPath);  
    }  
      
    /** 
     * 复制并合并文件夹， 
     * 不会替换目标文件夹中已经存在的文件或文件夹。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception  
     */  
    public static void copyAndMergerFolder(String srcPath,String desPath) throws Exception{       
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        File folder = getFolder(srcPath);  
        createFolder(desPath);  
        File[] files = folder.listFiles();  
        for(File file:files){  
            String src = file.getAbsolutePath();  
            String des = desPath+File.separator+file.getName();  
            if(file.isFile()){  
                copyAndNotReplaceFile(src,des);  
            }else if(file.isDirectory()){  
                copyAndMergerFolder(src,des);  
            }  
        }  
    }  
      
    /** 
     * 复制并替换文件夹， 
     * 将目标文件夹完全替换成源文件夹， 
     * 目标文件夹原有的资料会丢失。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception  
     */  
    public static void copyAndReplaceFolder(String srcPath,String desPath) throws Exception{     
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        File folder = getFolder(srcPath);  
        createNewFolder(desPath);  
        File[] files = folder.listFiles();  
        for(File file:files){  
            String src = file.getAbsolutePath();  
            String des = desPath+File.separator+file.getName();  
            if(file.isFile()){  
                copyAndReplaceFile(src,des);  
            }else if(file.isDirectory()){  
                copyAndReplaceFolder(src,des);  
            }  
        }  
    }   
      
    /** 
     * 合并文件夹后，将源文件夹删除。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception  
     */  
    public static void moveAndMergerFolder(String srcPath,String desPath) throws Exception{  
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);          
        copyAndMergerFolder(srcPath,desPath);  
        deleteFolder(srcPath);  
    }  
      
    /** 
     * 替换文件夹后，将源文件夹删除。 
     * @param srcPath 
     * @param desPath 
     * @throws Exception  
     */  
    public static void moveAndReplaceFolder(String srcPath,String desPath) throws Exception{  
        srcPath = separatorReplace(srcPath);  
        desPath = separatorReplace(desPath);  
        copyAndReplaceFolder(srcPath,desPath);  
        deleteFolder(srcPath);  
    }      
  
    /** 
     * 创建文件夹，如果文件夹存在则不进行创建。 
     * @param path 
     * @throws Exception  
     */  
    public static void createFolder(String path) throws Exception{  
        path = separatorReplace(path);  
        File folder = new File(path);  
        if(folder.isDirectory()){  
            return;  
        }else if(folder.isFile()){  
            deleteFile(path);  
        }  
        folder.mkdirs();  
    }  
      
    /** 
     * 创建一个新的文件夹，如果文件夹存在，则删除后再创建。 
     * @param path 
     * @throws Exception 
     */  
    public static void createNewFolder(String path) throws Exception{  
        path = separatorReplace(path);  
        File folder = new File(path);  
        if(folder.isDirectory()){  
            deleteFolder(path);  
        }else if(folder.isFile()){  
            deleteFile(path);  
        }  
        folder.mkdirs();  
    }  
      
    /** 
     * 创建一个文件，如果文件存在则不进行创建。 
     * @param path 
     * @throws Exception 
     */  
    public static File createFile(String path) throws Exception{  
        path = separatorReplace(path);  
        File file = new File(path);  
        if(file.isFile()){  
            return file;  
        }else if(file.isDirectory()){  
            deleteFolder(path);  
        }  
        return createFile(file);  
    }  
      
    /** 
     * 创建一个新文件，如果存在同名的文件或文件夹将会删除该文件或文件夹， 
     * 如果父目录不存在则创建父目录。 
     * @param path 
     * @throws Exception 
     */  
    public static File createNewFile(String path) throws Exception{  
        path = separatorReplace(path);  
        File file = new File(path);  
        if(file.isFile()){  
            deleteFile(path);  
        }else if(file.isDirectory()){  
            deleteFolder(path);  
        }  
        return createFile(file);  
    }  
      
    /** 
     * 分隔符替换 
     * window下测试通过 
     * @param path 
     * @return 
     */  
    public static String separatorReplace(String path){  
        return path.replace("\\","/");  
    }  
      
    /** 
     * 创建文件及其父目录。 
     * @param file 
     * @throws Exception 
     */  
    public static File createFile(File file) throws Exception{  
        createParentFolder(file);  
        if(!file.createNewFile()) {  
            throw new Exception("create file failure!");  
        }  
        return file;  
    }  
      
    /** 
     * 创建父目录 
     * @param file 
     * @throws Exception 
     */  
    private static void createParentFolder(File file) throws Exception{  
        if(!file.getParentFile().exists()) {  
            if(!file.getParentFile().mkdirs()) {  
                throw new Exception("create parent directory failure!");  
            }  
        }  
    }  
      
    /** 
     * 根据文件路径删除文件，如果路径指向的文件不存在或删除失败则抛出异常。 
     * @param path 
     * @return 
     * @throws Exception  
     */  
    public static void deleteFile(String path) throws Exception {  
        path = separatorReplace(path);  
        File file = getFile(path);      
        if(!file.delete()){  
            throw new Exception("delete file failure");  
        }                        
    }  
      
    /** 
     * 删除指定目录中指定前缀和后缀的文件。 
     * @param dir 
     * @param prefix 
     * @param suffix 
     * @throws Exception  
     */  
    public static void deleteFile(String dir,String prefix,String suffix) throws Exception{       
        dir = separatorReplace(dir);  
        File directory = getFolder(dir);  
        File[] files = directory.listFiles();  
        for(File file:files){  
            if(file.isFile()){  
                String fileName = file.getName();  
                if(fileName.startsWith(prefix)&&fileName.endsWith(suffix)){  
                    deleteFile(file.getAbsolutePath());  
                }  
            }  
        }      
    }  
      
    /** 
     * 根据路径删除文件夹，如果路径指向的目录不存在则抛出异常， 
     * 若存在则先遍历删除子项目后再删除文件夹本身。 
     * @param path 
     * @throws Exception  
     */  
    public static void deleteFolder(String path) throws Exception {  
        path = separatorReplace(path);  
        File folder = getFolder(path);  
        File[] files = folder.listFiles();   
        for(File file:files) {                  
            if(file.isDirectory()){  
                deleteFolder(file.getAbsolutePath());  
            }else if(file.isFile()){                      
                deleteFile(file.getAbsolutePath());                                   
            }  
        }    
        folder.delete();   
    }  
      
    /** 
     * 查找目标文件夹下的目标文件 
     * @param dir 
     * @param fileName 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static File searchFile(String dir,String fileName) throws FileNotFoundException{  
        dir = separatorReplace(dir);  
        File f = null;  
        File folder = getFolder(dir);  
        File[] files = folder.listFiles();   
        for(File file:files) {                  
            if(file.isDirectory()){  
                f =  searchFile(file.getAbsolutePath(),fileName);  
                if(f!=null){  
                    break;  
                }  
            }else if(file.isFile()){   
                if(file.getName().equals(fileName)){  
                    f = file;  
                    break;  
                }                                                             
            }  
        }            
        return f;  
    }  
            
    /** 
     * 获得文件类型。 
     * @param path：文件路径 
     * @return 
     * @throws FileNotFoundException  
     */  
    public static String getFileType(String path) throws FileNotFoundException {  
        path = separatorReplace(path);  
        File file = getFile(path);  
        String fileName = file.getName();  
        String[] strs = fileName.split("\\.");  
        if(strs.length<2){  
            return "unknownType";  
        }  
        return strs[strs.length-1];  
    }  
      
    /** 
     * 根据文件路径，获得该路径指向的文件的大小。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static long getFileSize(String path) throws FileNotFoundException{  
        path = separatorReplace(path);        
        File file = getFile(path);  
        return file.length();  
    }  
      
    /** 
     * 根据文件夹路径，获得该路径指向的文件夹的大小。 
     * 遍历该文件夹及其子目录的文件，将这些文件的大小进行累加，得出的就是文件夹的大小。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static long getFolderSize(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        long size = 0;  
        File folder = getFolder(path);  
        File[] files = folder.listFiles();  
        for(File file:files){  
            if(file.isDirectory()){  
                size += getFolderSize(file.getAbsolutePath());
            }else if(file.isFile()){  
                size += file.length();
            }  
        }  
        return size;  
    }  
      
    /**
     * @description 
     * 获取文件夹下全部文件名(不包含目录名),不包括子目录中文件名
     * @Author zhang bo
     * @createDate 2014年10月23日  下午6:21:26
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static List<String> getSubFileName(String path) throws FileNotFoundException{
        path = separatorReplace(path);                
        List<String> fileNameList = new ArrayList<String>();
        File folder = getFolder(path);  
        File[] files = folder.listFiles();  
        for(File file:files){  
        	String fileName = file.getName();
        	fileNameList.add(fileName);
        }  
        return fileNameList;
    }

    /**
     * @description
     * 获取文件夹下全部文件名(不包含目录名),包括子目录中文件名(递归)
     * @Author zhang bo
     * @createDate 2014年10月23日  下午6:21:26
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static List<String> getAllSubFileName(String path) throws FileNotFoundException{
        path = separatorReplace(path);
        List<String> fileNameList = new ArrayList<String>();
        File folder = getFolder(path);
        File[] files = folder.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                List<String> subList = getAllSubFileName(file.getAbsolutePath());
                fileNameList.addAll(subList);
            }else if(file.isFile()){
                String fileName = file.getName();
                fileNameList.add(fileName);
            }
        }
        return fileNameList;
    }

    /**
     * @description
     * 获取文件夹下全部文件名(全路径名称),包括子目录中文件名(递归)
     * @Author zhang bo
     * @createDate 2014年10月23日  下午6:21:26
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static List<String> getAllSubAbsolutePathFileName(String path) throws FileNotFoundException{
        path = separatorReplace(path);
        List<String> fileNameList = new ArrayList<String>();
        File folder = getFolder(path);
        File[] files = folder.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                List<String> subList = getAllSubAbsolutePathFileName(file.getAbsolutePath());
                fileNameList.addAll(subList);
            }else if(file.isFile()){
                fileNameList.add(file.getAbsolutePath());
            }
        }
        return fileNameList;
    }

    /**
     * @description
     * 获取文件夹下全部文件名(全路径名称),包括子目录中文件名(递归)。
     * 并且是suffix后缀的文件
     * @param path
     * @param suffix 文件后缀
     * @return
     * @throws FileNotFoundException
     */
    public static List<String> getAllSubAbsolutePathFileNameWithSuffix(String path, final String suffix) throws FileNotFoundException{
        path = separatorReplace(path);
        List<String> fileNameList = new ArrayList<String>();
        File folder = getFolder(path);
        File[] files = folder.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                List<String> subList = getAllSubAbsolutePathFileNameWithSuffix(file.getAbsolutePath(), suffix);
                fileNameList.addAll(subList);
            }else if(file.isFile()){
                String fileName = file.getName();
                if(StringUtils.isNotBlank(suffix) && fileName.endsWith(suffix)){
                    fileNameList.add(file.getAbsolutePath());
                }
            }
        }
        return fileNameList;
    }

    /** 
     * 通过路径获得文件， 
     * 若不存在则抛异常， 
     * 若存在则返回该文件。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static File getFile(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        File file = new File(path);  
        if(!file.isFile()){  
            throw new FileNotFoundException("file not found!");  
        }  
        return file;  
    }  
      
    /** 
     * 通过路径获得文件夹， 
     * 若不存在则抛异常， 
     * 若存在则返回该文件夹。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static File getFolder(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        File folder = new File(path);  
        if(!folder.isDirectory()){  
            throw new FileNotFoundException("folder not found!");  
        }  
        return folder;  
    }  
      
    /** 
     * 获得文件最后更改时间。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static Date getFileLastModified(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        File file = getFile(path);  
        return new Date(file.lastModified());  
    }  
      
    /** 
     * 获得文件夹最后更改时间。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     */  
    public static Date getFolderLastModified(String path) throws FileNotFoundException{  
        path = separatorReplace(path);        
        File folder = getFolder(path);  
        return new Date(folder.lastModified());  
    } 
    
    /**
     * 判断是否为目录，如果path为null，则返回false
     * 
     * @param path 文件路径
     * @return 如果为目录true
     */
    public static boolean isDirectory(String path) {
        return (path == null) ? false : new File(path).isDirectory();
    }
    
    /**
     * 判断是否为目录，如果file为null，则返回false
     * 
     * @param file 文件
     * @return 如果为目录true
     */
    public static boolean isDirectory(File file) {
        return (file == null) ? false : file.isDirectory();
    }

    /**
     * 判断是否为文件，如果path为null，则返回false
     * 
     * @param path 文件路径
     * @return 如果为文件true
     */
    public static boolean isFile(String path) {
        return (path == null) ? false : new File(path).isDirectory();
    }

    /**
     * 判断是否为文件，如果file为null，则返回false
     * 
     * @param file 文件
     * @return 如果为文件true
     */
    public static boolean isFile(File file) {
        return (file == null) ? false : file.isDirectory();
    }
    
    /**
     * 判断文件是否存在，如果path为null，则返回false
     * 
     * @param path 文件路径
     * @return 如果存在返回true
     */
    public static boolean exist(String path) {
        return (path == null) ? false : new File(path).exists();
    }
    
    /**
     * 判断文件是否存在，如果file为null，则返回false
     * 
     * @param file 文件
     * @return 如果存在返回true
     */
    public static boolean exist(File file) {
        return (file == null) ? false : file.exists();
    }

    /**
     * 是否存在匹配文件
     * 
     * @param directory 文件夹路径
     * @param regexp 文件夹中所包含文件名的正则表达式
     * @return 如果存在匹配文件返回true
     */
    public static boolean exist(String directory, String regexp) {
        File file = new File(directory);
        if (!file.exists()) {
            return false;
        }

        String[] fileList = file.list();
        if (fileList == null) {
            return false;
        }

        for (String fileName : fileList) {
            if (fileName.matches(regexp)) {
                return true;
            }

        }
        return false;
    }
    
    /**
     * 列出文件目录dir下以suffix结尾的子文件集合，非递归
     * <p>
     * 如果dir为null或不存在，则返回null
     * <p>
     * 如果dir不为目录，则返回null
     * <p>
     * 如果 suffix为null或""，则代表返回所有子文件
     * 
     * @param dir 文件目录
     * @param suffix 文件后缀
     * @return 目录dir下以suffix结尾的子文件集合，非递归
     */
    public static File[] listDirSuffixFiles(File dir, final String suffix) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists() || dir.isFile()) {
            return null;
        }

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return StringUtils.isEmpty(suffix) ? true : (file.getName().endsWith(suffix));
            }
        });
    }

    /**
     * 列出文件目录dirPath下以suffix结尾的子文件集合，非递归
     * <p>
     * 如果dirPath为null或不存在，则返回null
     * <p>
     * 如果dirPath不为目录，则返回null
     * <p>
     * 如果 suffix为null或""，则代表返回所有子文件
     * 
     * @param dirPath 文件目录
     * @param suffix 文件后缀
     * @return 目录dirPath下以suffix结尾的子文件集合，非递归
     */
    public static File[] listDirSuffixFiles(String dirPath, final String suffix) {
        if (!exist(dirPath)) {
            return null;
        }
        File dir = new File(dirPath);

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return StringUtils.isEmpty(suffix) ? true : (file.getName().endsWith(suffix));
            }
        });
    }

    /**
     * 列出文件目录dir下满足所有条件conditions的子文件集合，非递归
     * <p>
     * 如果dir为null或不存在，则返回null
     * <p>
     * 如果dir不为目录，则返回null
     * <p>
     * 如果conditions为null，则认为无条件限制
     * 
     * @param dir 文件目录
     * @param conditions 过滤条件
     * @return 目录dir下满足所有条件conditions的子文件集合，非递归
     */
    public static File[] listDirAllConditionFiles(File dir, final boolean...conditions) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists() || dir.isFile()) {
            return null;
        }

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (null == conditions || conditions.length == 0) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (!condition) {
                        return false;
                    }
                }

                return true;
            }
        });
    }

    /**
     * 列出文件目录dirPath下满足所有条件conditions的子文件集合，非递归
     * <p>
     * 如果dirPath为null或不存在，则返回null
     * <p>
     * 如果dirPath不为目录，则返回null
     * <p>
     * 如果conditions为null，则认为无条件限制
     * 
     * @param dirPath 文件目录
     * @param conditions 过滤条件
     * @return 目录dirPath下满足所有条件conditions的子文件集合，非递归
     */
    public static File[] listDirAllConditionFiles(String dirPath, final boolean...conditions) {
        if (!exist(dirPath)) {
            return null;
        }
        File dir = new File(dirPath);

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (null == conditions || conditions.length == 0) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (!condition) {
                        return false;
                    }
                }

                return true;
            }
        });
    }

    /**
     * 列出文件目录dir下满足任一条件conditions的子文件集合，非递归
     * <p>
     * 如果dir为null或不存在，则返回null
     * <p>
     * 如果dir不为目录，则返回null
     * <p>
     * 如果conditions为null，则认为无条件限制
     * 
     * @param dir 文件目录
     * @param conditions 过滤条件
     * @return 目录dir下满足任一条件conditions的子文件集合，非递归
     */
    public static File[] listDirAnyConditionFiles(File dir, final boolean...conditions) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists() || dir.isFile()) {
            return null;
        }

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (null == conditions || conditions.length == 0) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (condition) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    /**
     * 列出文件目录dirPath下满足任一条件conditions的子文件集合，非递归
     * <p>
     * 如果dirPath为null或不存在，则返回null
     * <p>
     * 如果dirPath不为目录，则返回null
     * <p>
     * 如果conditions为null，则认为无条件限制
     * 
     * @param dirPath 文件目录
     * @param conditions 过滤条件
     * @return 目录dirPath下满足任一条件conditions的子文件集合，非递归
     */
    public static File[] listDirAnyConditionFiles(String dirPath, final boolean...conditions) {
        if (!exist(dirPath)) {
            return null;
        }
        File dir = new File(dirPath);

        return dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (null == conditions || conditions.length == 0) {
                    return true;
                }
                for (boolean condition : conditions) {
                    if (condition) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    /**
     * 简单工厂
     * 
     * @param filename 文件名
     * @return new File(filename)
     */
    public static File file(String filename) {
        if (filename == null) {
            return null;
        }
        return new File(filename);
    }

    /**
     * 简单工厂
     * 
     * @param parent 父目录
     * @param child 子文件
     * @return new File(parent, child);
     */
    public static File file(File parent, String child) {
        if (child == null) {
            return null;
        }

        return new File(parent, child);
    }

    /**
     * 文件、文件夹重命名
     * @param fileDir 文件目录
     * @param fileName 修改前文件名称、文件夹名称
     * @param reName 修改后文件名称、文件夹名称
     *
     * 注意：
     *     在Windows下，文件夹与文件名称是不区分大小写的。
     *     因此f:/A与f:/a其实是一码事。
     *     更名文件、文件夹的父路径必须相同，即必须在同一目录下。
     */
    public static void renameFile(String fileDir, String fileName, String reName){
        //想命名的原文件的路径
        File source = new File(fileDir+File.separator+fileName);
        if (!source.exists()) {
           return;
        }
        File reFile = new File(fileDir+File.separator+reName);
        if (reFile.exists()) {
            return;
        }
        //将原文件更改为新的文件名
        source.renameTo(reFile);
    }
}
