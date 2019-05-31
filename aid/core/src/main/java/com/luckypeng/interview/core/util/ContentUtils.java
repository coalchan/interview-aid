package com.luckypeng.interview.core.util;

import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.model.ContentType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * 习题需要以 ## 为开头
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
    public static final String CONTENT_ROOT_ID = "0";

    /**
     * 读取所有文件
     * @param contentPath
     * @return
     */
    public static Content listContent(String contentPath) {
        File contents = new File(contentPath);
        AssertionUtils.isTrue(contents.exists() && contents.isDirectory(), "目录不存在: " + contentPath);
        try {
            return recursionListContents(contents, CONTENT_ROOT_ID);
        } catch (IOException e) {
            throw new RuntimeException("读取文件报错: ", e);
        }
    }

    /**
     * 递归读取文件
     * @param contents
     * @throws IOException
     */
    public static Content recursionListContents(File contents, String parentId) throws IOException {
        File readmeFile = new File(contents.getAbsolutePath() + File.separator + README_FILE_NAME);
        AssertionUtils.isTrue(readmeFile.exists(), "文件夹中没有包含 README.md 文件: " + contents.getName());
        Content content = readFile(readmeFile, parentId);
        File[] files = contents.listFiles((dir, name) -> !README_FILE_NAME.equals(name));
        for (File file : files) {
            if (file.isFile()) {
                Content child = readFile(file, parentId);
                content.getChildren().put(child.getId(), child);
            } else {
                content.getChildren().put(getId(file.getName()), recursionListContents(file, getId(file.getName())));
            }
        }
        return content;
    }

    /**
     * 解析文件
     * @param file
     * @return
     * @throws IOException
     */
    public static Content readFile(File file, String parentId) throws IOException {
        int exerciseNum = -1;
        boolean isREADME = README_FILE_NAME.equals(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Content content = new Content();
            List<Content> exercises = new ArrayList<>(50);
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
                    content.setId(parentId);
                    content.setType(ContentType.DIR);
                    if (content.getRawText() == null && !line.isEmpty()) {
                        content.setRawText(line);
                    } else if (content.getRawText() != null) {
                        content.setRawText(content.getRawText() + "\n" + line);
                    }
                } else {
                    // 读取普通文档的正文部分，即习题部分
                    content.setId(getId(file.getName()));
                    content.setType(ContentType.FILE);

                    if (line.startsWith(EXERCISE_START)) {
                        exerciseNum ++;
                        String[] array = line.substring(EXERCISE_START.length()).trim().split("\\. ");
                        Content exercise = new Content(ContentType.EXERCISE, array[0], array[1]);
                        exercises.add(exercise);
                    } else if (!exercises.isEmpty() && exercises.get(exerciseNum).getSolution() == null
                            && !line.isEmpty()) {
                        exercises.get(exerciseNum).setSolution(line);
                    } else if (!exercises.isEmpty() && exercises.get(exerciseNum).getSolution() != null){
                        Content child = exercises.get(exerciseNum);
                        child.setSolution(child.getSolution() + "\n" + line);
                    }
                }
            }
            if (!exercises.isEmpty()) {
                content.setExercises(exercises);
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

    /**
     * 修正 ID，小于 10 的 补 0
     * @param id
     * @return
     */
    public static String fixId(int id) {
        AssertionUtils.isTrue(id > 0, "序号必须大于 0");
        if (id >= 10) {
            return String.valueOf(id);
        } else {
            return "0" + id;
        }
    }
}
