/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springultron.doc.model;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendMarkdownChildren;
import com.github.xiaoymin.knife4j.core.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2019/04/17 19:54
 */
public class MarkdownFiles {
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final Logger logger = LoggerFactory.getLogger(MarkdownFiles.class);

    /***
     * markdown files dir
     */
    private String basePath;

    private List<OpenApiExtendMarkdownChildren> markdownFiles = new ArrayList<>();

    public MarkdownFiles() {
    }

    public MarkdownFiles(String basePath) {
        this.basePath = basePath;
    }

    public void init() {
        // 初始化
        if (basePath != null && !"".equals(basePath)) {
            try {
                Resource[] resources = resourceResolver.getResources(basePath);
                for (Resource resource : resources) {
                    OpenApiExtendMarkdownChildren markdownFile = createMarkdownFile(resource);
                    if (markdownFile != null) {
                        getMarkdownFiles().add(markdownFile);
                    }
                }
            } catch (Exception e) {
                logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", e.getMessage());
            }
        }
    }

    private OpenApiExtendMarkdownChildren createMarkdownFile(Resource resource) {
        OpenApiExtendMarkdownChildren markdownFile = new OpenApiExtendMarkdownChildren();
        if (resource != null) {
            String filename = resource.getFilename();
            logger.info("createMarkdownFile: {}", filename);
            // 只读取md
            if (filename != null && filename.toLowerCase().endsWith(".md")) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
                    String le;
                    String title = resource.getFilename();
                    String reg = "#{1,3}\\s{1}(.*)";
                    Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
                    Matcher matcher;
                    while ((le = reader.readLine()) != null) {
                        // 判断line是否是包含标题
                        matcher = pattern.matcher(le);
                        if (matcher.matches()) {
                            title = matcher.group(1);
                        }
                        break;
                    }
                    CommonUtils.close(reader);

                    markdownFile.setTitle(title);
                    markdownFile.setContent(new String(CommonUtils.readBytes(resource.getInputStream()), "UTF-8"));
                    return markdownFile;
                } catch (Exception e) {
                    logger.warn("(Ignores) Failed to read Markdown files,Error Message:{} ", e.getMessage());
                } finally {
                    CommonUtils.close(reader);
                }
            }
        }
        return null;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<OpenApiExtendMarkdownChildren> getMarkdownFiles() {
        return markdownFiles;
    }

    public void setMarkdownFiles(List<OpenApiExtendMarkdownChildren> markdownFiles) {
        this.markdownFiles = markdownFiles;
    }

}
