package com.rubber.app.publish.logic.manager.pack;

import cn.hutool.core.util.StrUtil;
import com.rubber.app.publish.core.constant.ErrCodeEnums;
import com.rubber.app.publish.core.entity.ApplicationConfigInfo;
import com.rubber.app.publish.core.entity.PublishTaskInfo;
import com.rubber.app.publish.logic.exception.AppPublishException;
import com.rubber.app.publish.logic.manager.pack.dto.MavenResolveDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author luffyu
 * Created on 2021/10/1
 */
@Slf4j
@Component
public class MavenResolveManager {


    /**
     * github的clone地址是https://github.com/luffyu-dev/rubber_common_utils.git
     *
     * github的下载连接测试 https://raw.githubusercontent.com/luffyu/rubber-admin/dev/springboot-rubber-admin-start/pom.xml
     */
    private static final String GIT_HUB_API_DOWNLOAD = "https://raw.githubusercontent.com";
    private static final String GIT_HUB_CLONE = "https://github.com";



    /**
     * 解析当前jar的版本
     * @param publishTaskInfo
     */
    public void resolveVersion(PublishTaskInfo publishTaskInfo, ApplicationConfigInfo applicationConfigInfo) {
        MavenResolveDto mavenResolveDto = new MavenResolveDto();
        BeanUtils.copyProperties(publishTaskInfo,mavenResolveDto);
        BeanUtils.copyProperties(applicationConfigInfo,mavenResolveDto);
        resolveVersion(mavenResolveDto);
        publishTaskInfo.setJarName(mavenResolveDto.getJarName());
        publishTaskInfo.setJarVersion(mavenResolveDto.getJarVersion());
    }


    /**
     * 解析当前jar的版本
     */
    public void resolveVersion(MavenResolveDto mavenResolveDto) {
        //maven版本的解析
        String gitHubDownloadUrl = resolveDownloadUrl(mavenResolveDto);
        log.info("下载的连接是：{}",gitHubDownloadUrl);
        InputStreamReader streamReader = null;
        try {
            try {
                URL url = new URL(gitHubDownloadUrl);
                HttpURLConnection hur = (HttpURLConnection) url.openConnection();
                streamReader = new InputStreamReader(hur.getInputStream());

            } catch (Exception e) {
                log.error("从github上下载pom.xml异常 {}",e.getMessage());
                throw new AppPublishException(ErrCodeEnums.DOWNLOAD_GITHUB_POM_ERROR,"下载的连接是{},异常{}",gitHubDownloadUrl,e.getMessage());
            }
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(streamReader);
            if (model == null){
                throw new AppPublishException(ErrCodeEnums.RESOLVE_MAVEN_ERROR);
            }
            if (model.getBuild() == null || StringUtils.isEmpty(model.getBuild().getFinalName())){
                throw new AppPublishException(ErrCodeEnums.RESOLVE_MAVEN_ERROR,"{} pom.xml没有在build中配置finaleName",mavenResolveDto.getPublishModel());
            }
            mavenResolveDto.setJarName(model.getBuild().getFinalName() + ".jar");
            mavenResolveDto.setJarVersion(model.getVersion());
        }catch (Exception me){
            if (me instanceof AppPublishException){
                throw (AppPublishException)me;
            }else {
                throw new AppPublishException(ErrCodeEnums.PARAM_ERROR,"解析maven异常{}",me.getMessage());
            }
        }finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                }catch (Exception e){
                    log.error("streamReader.close msg={} ",e.getMessage());
                }
            }
        }
    }
    private String resolveDownloadUrl(MavenResolveDto mavenResolveDto){
        //maven版本的解析
        String gitHubUrl = mavenResolveDto.getGithubUrl();
        String downloadUrl = formatterToDownloadApi(gitHubUrl);
        StringBuilder sb = new StringBuilder(downloadUrl);
        sb.append("/").append(mavenResolveDto.getAppPackTag()).append("/");
        if (StrUtil.isNotEmpty(mavenResolveDto.getPublishModel())){
            sb.append(mavenResolveDto.getPublishModel()).append("/");
        }
        sb.append("pom.xml");
        return sb.toString();
    }


    private String formatterToDownloadApi(String cloneUrl){
        String download = cloneUrl.replace("https://github.com",GIT_HUB_API_DOWNLOAD);
        if (download.endsWith(".git")){
            download = download.substring(0, download.lastIndexOf(".git"));
        }
        return download;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(1);

        URL url = new URL("https://raw.githubusercontent.com/luffyu/rubber-admin/dev/springboot-rubber-admin-start/pom.xml");

        HttpURLConnection hur = (HttpURLConnection)url.openConnection();

        InputStreamReader streamReader = new InputStreamReader(hur.getInputStream());



        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(streamReader);

        //Model model = reader.read(new FileReader("pom.xml"));

        System.out.println(model.getId());

        System.out.println(model.getGroupId());

        System.out.println(model.getArtifactId());

        System.out.println(model.getVersion());

    }


}
