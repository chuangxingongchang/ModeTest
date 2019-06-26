package com.test.DDUtil;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListIdsRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendprogressRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetAdminRequest;
import com.dingtalk.api.request.OapiUserGetDeptMemberRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetUseridByUnionidRequest;
import com.dingtalk.api.request.OapiUserListbypageRequest;
import com.dingtalk.api.request.OapiUserSimplelistRequest;
import com.dingtalk.api.response.OapiDepartmentListIdsResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendprogressResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetAdminResponse;
import com.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetUseridByUnionidResponse;
import com.dingtalk.api.response.OapiUserListbypageResponse;
import com.dingtalk.api.response.OapiUserSimplelistResponse;
import com.taobao.api.ApiException;
import com.util.DbFH;
import com.test.entity.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;


/**
 * 钉钉接口调用工具
 * @author lengdl
 * @2019年6月15日15:03:45
 */
public class DingDingUtil {

    /**打印日志设置*/
    private static final Logger log =  LoggerFactory.getLogger(DingDingUtil.class);

    /**企业corpid*/
    private static String CROP_ID = "ding478e079ef2f281f435c2f4657eb6378f";

    /**企业corpsecret*/
    private static String CROP_SECRET = "bDPKKOPZh_KBe2h2d-syc_xQKglCV_Oj0Dd6jKt33oAESM0I9t2j_6I7CnDMrtKr";

    /**应用id*/
    private static String APP_ID = "dingoa83vywlfqfxhtqyoz";

    /**应用secret*/
    private static String APP_SECRET = "sC0w0EoiMV-S74l5ZS9QBraUgHGNhYqunV3LEjFx1Zu2cD9J220QkKdBkNXTtDKF";

    /**自动分配微应用的ID*/
    public static  Long AgentId = 249390936L;

    /**
     * @Description :获取用户登录信息通过code
     * @param code
     * @return
     * @throws ApiException
     */
    public static OapiSnsGetuserinfoBycodeResponse SnsGetUserinfoBycode(String code) {

        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sns/getuserinfo_bycode");

        OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();

        req.setTmpAuthCode(code);

        OapiSnsGetuserinfoBycodeResponse response = null;
        try {
            response = client.execute(req,APP_ID,APP_SECRET);

        } catch (ApiException e) {
           log.error(">>Msg.",e);
        }

        return  response;
    }

    /**
     * @Description 通过unionid获取userID
     * @param unionid
     * @param accessToken
     * @return
     * @throws ApiException
     */
    public static OapiUserGetUseridByUnionidResponse userGetUseridByUnionid(String unionid,String accessToken) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getUseridByUnionid");

        OapiUserGetUseridByUnionidRequest request = new OapiUserGetUseridByUnionidRequest();

        request.setUnionid(unionid);

        OapiUserGetUseridByUnionidResponse response = null;
        try {
            response = client.execute(request,accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }

        return response;
    }

    /**
     * @Description 根据企业的cropid和cropsecret获取access_token
     *正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期
     * @return
     * @throws ApiException
     */
    public static OapiGettokenResponse getAccessToken(){

        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");

        OapiGettokenRequest request = new OapiGettokenRequest();

        request.setCorpid(CROP_ID);

        request.setCorpsecret(CROP_SECRET);

        request.setHttpMethod("GET");

        OapiGettokenResponse  response = null;

        try {
            response = client.execute(request);
        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }

        return response;
    }

    /**
     * @Description 根据应用的appid和appsecret获取access_token
     *正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期
     * @return
     * @throws ApiException
     */
    public static OapiGettokenResponse getAccessTokenByApp(){

        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");

        OapiGettokenRequest request = new OapiGettokenRequest();

        request.setAppkey(APP_ID);

        request.setAppsecret(APP_SECRET);

        request.setHttpMethod("GET");

        OapiGettokenResponse response = null;
        try {
            response = client.execute(request);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
            e.printStackTrace();
        }

        return response;
    }

    /**
     * @Description 通过用户userid获取用户详情
     * @param userid      用户id
     * @param accessToken 调用接口凭证
     * @return
     */
    public static OapiUserGetResponse getUserinfo(String userid,String accessToken){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");

        OapiUserGetRequest request = new OapiUserGetRequest();

        request.setUserid(userid);

        request.setHttpMethod("GET");

        OapiUserGetResponse response = null;
        try {
            response = client.execute(request,accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }

        return response;
    }

    /**
     * 通过部门id获取部门用户列表
     * @param departmentId 部门id（如果不传，默认部门为根部门，根部门ID为1
     * @param accessToken  调用接口凭证
     * @param pageNum      页码
     * @param pageSize     每页条数
     * @return
     */
    public static OapiUserSimplelistResponse getDepartmentUserList(long departmentId, String accessToken, long pageNum, long pageSize){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/simplelist");

        OapiUserSimplelistRequest request = new OapiUserSimplelistRequest();

        request.setDepartmentId(departmentId);

        request.setOffset(pageNum);

        request.setSize(pageSize);

        request.setHttpMethod("GET");

        OapiUserSimplelistResponse response = null;
        try {
            response = client.execute(request, accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }
        return response;
    }

    /**
     * 获取部门用户userid列表
     * @param departmentId 部门id（如果不传，默认部门为根部门，根部门ID为1
     * @param accessToken  调用接口凭证
     * @return
     */
    public static OapiUserGetDeptMemberResponse getDepartmentUserList(String departmentId, String accessToken){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");

        OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();

        req.setDeptId(departmentId);

        req.setHttpMethod("GET");

        OapiUserGetDeptMemberResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }
        return rsp;
    }

    /**
     * 通过部门id获取部门用户详情
     * @param departmentId 部门id
     * @param accessToken
     * @param pageNum
     * @param pageSize
     * @throws ApiException
     */
    public static OapiUserListbypageResponse getDepartmentUserInfoList(long departmentId, String accessToken, long pageNum, long pageSize){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");

        OapiUserListbypageRequest request = new OapiUserListbypageRequest();

        request.setDepartmentId(departmentId);

        request.setOffset(pageNum);

        request.setSize(pageSize);

        request.setOrder("entry_desc");

        request.setHttpMethod("GET");

        OapiUserListbypageResponse response = null;
        try {
            response = client.execute(request, accessToken);

        } catch (ApiException e) {

            log.error(">>Msg.",e);
        }
        return response;
    }

    /**
     * 获取管理员列表
     * @param accessToken
     * @throws ApiException
     */
    public static OapiUserGetAdminResponse getAdminList(String accessToken){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_admin");

        OapiUserGetAdminRequest request = new OapiUserGetAdminRequest();

        request.setHttpMethod("GET");

        OapiUserGetAdminResponse response = null;
        try {
            response = client.execute(request, accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }
        return response;
    }

    /**
     * 通过父部门id获取子部门列表
     * @param id          父部门id（如果不传，默认部门为根部门，根部门ID为1
     * @param accessToken 调用接口凭证
     * @param fetchChild  是否递归部门的全部子部门，ISV微应用固定传递false
     * @return
     */
    public static OapiDepartmentListResponse getDepartmentList(String id, String accessToken, boolean fetchChild) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");

        OapiDepartmentListRequest request = new OapiDepartmentListRequest();

        request.setId(id);

        request.setFetchChild(fetchChild);

        request.setHttpMethod("GET");

        OapiDepartmentListResponse response = null;
        try {
            response = client.execute(request, accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }

        return response;
    }

    /**
     * 通过父部门id获取子部门id列表
     * @param id          父部门id（如果不传，默认部门为根部门，根部门ID为1
     * @param accessToken 调用接口凭证
     * @return
     */
    public static OapiDepartmentListIdsResponse getDepartmentIdList(String id, String accessToken){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list_ids");

        OapiDepartmentListIdsRequest request = new OapiDepartmentListIdsRequest();

        request.setId(id);

        request.setHttpMethod("GET");

        OapiDepartmentListIdsResponse response = null;
        try {
            response = client.execute(request, accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.",e);
        }
        return response;
    }

    /**
     * 钉钉消息推送
     * <p>Title: sendMessege</p>
     * <p>Description: </p>
     * @param dduserId 钉钉ID
     * @param tital  头部信息
     * @param disMessege 钉钉显示消息
     * @param text 钉钉提示内容
     * @param url 钉钉指向地址 URLEncoder 类型
     * @throws ApiException
     * @throws UnsupportedEncodingException
     * @author lcb
     */
    public static Boolean sendMessege(String dduserId,String tital,String disMessege,String text,String url) throws ApiException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

        Properties pros;
        pros = DbFH.getMesPushSwitch();

        String mesPushSwitch;	//消息开关
        mesPushSwitch = pros.getProperty("MesPushSwitch");

        if(StringUtils.equals("1",mesPushSwitch)) {

            return false;
        }
        url = pros.getProperty("dingMessegUrl")+url;

        url = url.replaceAll("&", "%26");

        url = url.replaceAll("=", "%3D");

        OapiMessageCorpconversationAsyncsendV2Request request;
        request = new OapiMessageCorpconversationAsyncsendV2Request();

        request.setUseridList(dduserId);

        request.setAgentId(AgentId);

        request.setToAllUser(false);
        /**在PC客户端点击消息中的URL链接时，希望在PC客户端打开而不是外跳到浏览器，可以使用下面方式：
         dingtalk://dingtalkclient/page/link?url=http%3A%2F%2Fwww.dingtalk.com&pc_slide=true
         参数说明:

         url参数，表示要打开的链接，必须urlEncode

         pc_slide参数，如果为true表示在PC客户端侧边栏打开，为false或者不传表示用浏览器打开*/
        String dingUrl = "dingtalk://dingtalkclient/page/link?url="+url+"&pc_slide=true";

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg;
        msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("action_card");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        OapiMessageCorpconversationAsyncsendV2Request.ActionCard acard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
        acard.setTitle(tital);
        acard.setMarkdown(new StringBuilder().append("**").append(disMessege).append("**  \n  ").append(text).toString());
        acard.setSingleTitle("查看详情");

        acard.setSingleUrl(dingUrl);

        msg.setActionCard(acard);

        request.setMsg(msg);

        String accessToken = getAccessToken().getAccessToken();

        OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,accessToken);

        if(Objects.isNull(response)||StringUtils.isBlank(String.valueOf(response.getTaskId()))){
            return false;
        }
        DingTalkClient clients;
        clients = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress");

        OapiMessageCorpconversationGetsendprogressRequest req;

        req = new OapiMessageCorpconversationGetsendprogressRequest();

        req.setAgentId(AgentId);

        req.setTaskId(response.getTaskId());

        try {
            OapiMessageCorpconversationGetsendprogressResponse resp;

            resp = clients.execute(req, accessToken);

            if(!StringUtils.equals("2", String.valueOf(resp.getProgress().getStatus()))){
                return false;
            }
        } catch (ApiException e) {
            log.error(">>Msg.",e);
            return false;
        }

        return true;
    }
    /**
     * @Description :发送钉钉消息
     * @Method :sendDDMessage
     * @Auther :leng_dl
     * @param user 接收者的用户列表
     * @return
     */
    public static Boolean sendJobNoticeMsg(User user , String Title, String content, String url){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        /**可选userid_list,dept_id_list,接收者的用户userid列表,最大列表长度：100*/
        request.setUseridList(user.getDdUserId());
        request.setAgentId(AgentId);
        /**是否发送给企业全部用户*/
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        msg.getText().setContent("test123");
        request.setMsg(msg);

        msg.setMsgtype("image");
        msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
        msg.getImage().setMediaId("@lADOdvRYes0CbM0CbA");
        request.setMsg(msg);

        msg.setMsgtype("file");
        msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
        msg.getFile().setMediaId("@lADOdvRYes0CbM0CbA");
        request.setMsg(msg);

        msg.setMsgtype("link");
        msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
        msg.getLink().setTitle("test");
        msg.getLink().setText("test");
        msg.getLink().setMessageUrl("test");
        msg.getLink().setPicUrl("test");
        request.setMsg(msg);

        msg.setMsgtype("markdown");
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        msg.getMarkdown().setText("##### text");
        msg.getMarkdown().setTitle("### Title");
        request.setMsg(msg);

        msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
        msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
        msg.getOa().getHead().setText("head");
        msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
        msg.getOa().getBody().setContent("xxx");
        msg.setMsgtype("oa");
        request.setMsg(msg);


        msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
        msg.getActionCard().setTitle(Title);
        msg.getActionCard().setMarkdown(content);
        msg.getActionCard().setSingleTitle("测试测试");
        msg.getActionCard().setSingleUrl(url);
        msg.setMsgtype("action_card");
        request.setMsg(msg);
        String accessToken = getAccessToken().getAccessToken();
        OapiMessageCorpconversationAsyncsendV2Response response = null;
        try {
            response = client.execute(request,accessToken);
        } catch (ApiException e) {
            log.error(">>Msg.",e);
            e.printStackTrace();
        }
        if(Objects.isNull(response)||StringUtils.isBlank(String.valueOf(response.getTaskId()))){
            return false;
        }
        DingTalkClient clients = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress");
        OapiMessageCorpconversationGetsendprogressRequest req  = new OapiMessageCorpconversationGetsendprogressRequest();
        req.setAgentId(AgentId);
        req.setTaskId(response.getTaskId());
        try {
            OapiMessageCorpconversationGetsendprogressResponse resp = clients.execute(req, accessToken);
            if(!StringUtils.equals("2", String.valueOf(resp.getProgress().getStatus()))){
                return false;
            }
        } catch (ApiException e) {
            log.error(">>Msg.",e);
            return false;
        }

        return true;
    }


    public static String randNoce(){

        String result = "";

        Random random = new Random((Long) System.currentTimeMillis());

        String textArray = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        try {
            for(int i = 0;i < 8;i++){

                /**时间戳可能大于int范围*/
                int temp;
                temp = Math.abs(random.nextInt())%textArray.length();

                result = result + textArray.substring(temp,temp+1);
            }
        }catch (Exception e){
            log.error(">>Msg.",e);
        }
        return result;
    }
}
