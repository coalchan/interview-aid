package com.luckypeng.interview.core.util;

import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.model.ContentType;

import java.io.*;
import java.util.*;

/**
 * @author coalchan
 */
public class ContentUtils {
    private ContentUtils() {}

    /**
     * 文件规范: 需要有文件头，例如
     * ---
     * title: 标题
     * tags: [标签1, 标签2, 标签3]
     * desc: 这里是描述
     * ---
     */
    private static final String SEPARATOR_LINE = "---";
    private static final String COLON_TAG = ": ";

    /**
     * 题目需要以 ## 为开头
     */
    private static final String EXERCISE_START = "## ";
    private static final String README_FILE_NAME = "README.md";

    /**
     * 文件名规范: 序号-标题
     */
    private static final String SEPARATOR_FILE_NAME = "-";

    /**
     * 根路径名称
     */
    private static final String CONTENT_ROOT_ID = "0";

    /**
     * 读取所有文件
     * @param contentPath
     * @return
     */
    public static Map<String, Content> listContent(String contentPath) {
        File contents = new File(contentPath);
        AssertionUtils.isTrue(contents.exists() && contents.isDirectory(), "目录不存在: " + contentPath);
        Map<String, Content> result = new HashMap<>(1000);
        try {
            recursionListContents(contents, "", result);
        } catch (IOException e) {
            throw new RuntimeException("读取文件报错: ", e);
        }
        return result;
    }

    /**
     * 递归读取文件
     * @param contents
     * @param parentId
     * @param result
     * @throws IOException
     */
    public static void recursionListContents(
            File contents, String parentId, Map<String, Content> result) throws IOException {
        File[] files = contents.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                Content content = readFile(file, parentId);
                result.put(content.getId().isEmpty() ? CONTENT_ROOT_ID : content.getId(), content);
                if (ObjectUtils.isNotEmpty(content.getChildren())) {
                    content.getChildren().forEach(c -> result.put(c.getId(), c));
                }
            } else {
                String currentId = (parentId.isEmpty() ? parentId : parentId + SEPARATOR_FILE_NAME)
                        + getId(file.getName());
                recursionListContents(file, currentId, result);
            }
        }
    }

    /**
     * 解析文件
     * @param file
     * @param parentId
     * @return
     * @throws IOException
     */
    public static Content readFile(File file, String parentId) throws IOException {
        int exerciseNum = -1;
        boolean isREADME = README_FILE_NAME.equals(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Content content = new Content();
            // Id 拼接
            content.setId(isREADME ? parentId : parentId + SEPARATOR_FILE_NAME + getId(file.getName()));
            List<Content> children = new ArrayList<>(50);
            int separatorNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(SEPARATOR_LINE)) {
                    separatorNum ++;
                } else if (line.startsWith(Content.FILE_KEY_TITLE)) {
                    content.setTitle(line.substring(Content.FILE_KEY_TITLE.length() + COLON_TAG.length()).trim());
                } else if (line.startsWith(Content.FILE_KEY_DESC)) {
                    content.setDesc(line.substring(Content.FILE_KEY_DESC.length() + COLON_TAG.length()).trim());
                } else if (line.startsWith(Content.FILE_KEY_TAGS)) {
                    String tags = line.substring(Content.FILE_KEY_TAGS.length() + COLON_TAG.length()).trim();
                    content.setTags(Arrays.asList(tags.substring(1, tags.length() - 1).trim().split(" *, *")));
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
                        String[] array = line.substring(EXERCISE_START.length()).trim().split("\\. ");
                        Content child = new Content(
                                ContentType.EXERCISE, content.getId() + SEPARATOR_FILE_NAME + array[0], array[1]);
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

    /**
     * 截取文件名中的 ID
     * @param fileName
     * @return
     */
    public static String getId(String fileName) {
        String[] array = fileName.split(SEPARATOR_FILE_NAME);
        AssertionUtils.isTrue(array.length >= 2, "文件命名格式有误: " + fileName);
        return array[0];
    }
}
