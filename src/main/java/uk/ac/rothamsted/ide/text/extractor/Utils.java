package uk.ac.rothamsted.ide.text.extractor;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.tika.io.FilenameUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class Utils {
    private static final Logger logger = LogManager.getLogger(Utils.class);

    public static File createFileOrDirIfNotExist(String fileOrDirName) {
        File newFileOrDir = null;
        if (fileOrDirName != null){
            try{
                newFileOrDir = new File(fileOrDirName);
                if (!newFileOrDir.exists()){
                    boolean success = (new File(fileOrDirName)).mkdir();
                    if (success){
                        logger.info("File or Directory " + newFileOrDir + " created\n");
                    } else {
                        logger.warn("File or Directory not set");
                    }
                }
            } catch (Exception e){
                logger.warn("File or Directory not set");
            }
        }
        return newFileOrDir;
    }

    public static Map<String, String> initFileIndex(String fileOrDirName) {

        File fileOrDir = new File(fileOrDirName);
        Map<String, String> corpus = new TreeMap<>();
        String filePath;

        if(fileOrDir.isDirectory()){
            String[] list = fileOrDir.list();
            assert list != null;
            List<String> files = Arrays.asList(list);
            Collections.sort(files);

            for(String fileName : files){
                String fileSuffix = FilenameUtils.getSuffixFromPath(fileName);
                if (!fileSuffix.isEmpty()){
                    String documentId = fileName.replaceFirst(fileSuffix, "");
                    filePath = fileOrDirName + "/" + fileName;
                    corpus.put(documentId, filePath);
                }
            }
        }else {
            String fileName = fileOrDir.getName().split("/")[fileOrDir.getName().split("/").length - 1];
            filePath = fileOrDirName + "/" + fileName;
            corpus.put(fileName, filePath);
        }
        return corpus;
    }

    public static void saveToFile(String textContent, String fileName) throws IOException {
        RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
        FileChannel channel = stream.getChannel();
        byte[] strBytes = textContent.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
        buffer.put(strBytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();
    }
}
