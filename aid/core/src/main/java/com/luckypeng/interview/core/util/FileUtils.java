package com.luckypeng.interview.core.util;

import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.model.ContentType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coalchan
 */
public class FileUtils {
    private FileUtils() {}

    private static final String SEPARATOR_LINE = "---";
    private static final String COLON_TAG = ": ";
    private static final String DOT_TAG_REGEX = "\\. ";
    private static final String EXERCISE_START = "## ";

    public static Content read(String fileName, boolean isREADME) throws IOException {
        File file = new File(fileName);

        int exerciseNum = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Content content = new Content();
            List<Content> children = new ArrayList<>(50);
            int separatorNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(SEPARATOR_LINE)) {
                    separatorNum ++;
                } else if (line.startsWith(Content.FILE_KEY_TITLE)) {
                    content.setTitle(line.substring(Content.FILE_KEY_TITLE.length() + COLON_TAG.length()));
                } else if (line.startsWith(Content.FILE_KEY_DESC)) {
                    content.setDesc(line.substring(Content.FILE_KEY_DESC.length() + COLON_TAG.length()));
                } else if (line.startsWith(Content.FILE_KEY_TAGS)) {
                    // TODO
                } else if (separatorNum < 2) {
                    throw new RuntimeException("文件没有标准的文件头");
                } else if (isREADME) {
                    // 读取 README 文档的正文部分
                    content.setType(ContentType.DIR);
                    if (content.getRawText() == null && !line.isEmpty()) {
                        content.setRawText(line);
                    } else if (content.getRawText() != null) {
                        content.setRawText(content.getRawText() + "\n" + line);
                    }
                } else {
                    // 读取普通文档的正文部分，即面试题部分
                    content.setType(ContentType.FILE);

                    if (line.startsWith(EXERCISE_START)) {
                        exerciseNum ++;
                        String[] array = line.substring(EXERCISE_START.length()).split(DOT_TAG_REGEX);
                        Content child = new Content();
                        child.setId(array[0]);
                        child.setTitle(array[1]);
                        child.setType(ContentType.EXERCISE);
                        children.add(child);
                    } else if (!children.isEmpty() && children.get(exerciseNum).getSolution() == null
                            && !line.isEmpty()) {
                        children.get(exerciseNum).setSolution(line);
                    } else if (!children.isEmpty() && children.get(exerciseNum).getSolution() != null){
                        Content child = children.get(exerciseNum);
                        child.setSolution(child.getSolution() + "\n" + line);
                    }
                }
            }
            if (!children.isEmpty()) {
                content.setChildren(children);
            }
            return content;
        }
    }

    public static void main(String[] args) throws Exception {
        Content content = read("/Users/coalchan/gitcode/Others/interview-aid/contents/01-java/01-jvm.md",
                false);
        System.out.println();
    }
}
