package com.test.DDUtil;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetleavestatusRequest;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.request.OapiRoleGetroleRequest;
import com.dingtalk.api.request.OapiRoleGetrolegroupRequest;
import com.dingtalk.api.request.OapiRoleListRequest;
import com.dingtalk.api.request.OapiRoleSimplelistRequest;
import com.dingtalk.api.response.OapiAttendanceGetleavestatusResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiRoleGetroleResponse;
import com.dingtalk.api.response.OapiRoleGetrolegroupResponse;
import com.dingtalk.api.response.OapiRoleListResponse;
import com.dingtalk.api.response.OapiRoleSimplelistResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import com.test.entity.AttendanceGetleave;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DDGetRoles {

    private static final Logger  log = LoggerFactory.getLogger(DDGetRoles.class);

    public static  String accessToken = DingDingUtil.getAccessToken().getAccessToken();

    /**生成DateFormat类对象的固定格式*/
    public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**生成DateFormat类对象的固定格式*/
    public static DateFormat dff = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取打卡结果,根据返回的result判断用户的状态
     * @param startTime  查询考勤打卡记录的起始工作日。格式为“yyyy-MM-dd HH:mm:ss”，
     * @param endTime 查询考勤打卡记录的结束工作日,格式为“yyyy-MM-dd HH:mm:ss”
     * @param useridList 员工在企业内的UserID列表，企业用来唯一标识用户的字段。最多不能超过50个
     * @param offset 表示获取考勤数据的起始点，第一次传0，如果还有多余数据，下次获取传的offset值为之前的offset+limit，0、1、2...依次递增
     * @param limit  表示获取考勤数据的条数，最大不能超过50条
     * @return
     */
    public static OapiAttendanceListResponse getAttendanceList(
            String startTime, String endTime, List<String> useridList, Long offset, Long limit) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");

        OapiAttendanceListRequest request = new OapiAttendanceListRequest();

        /**查询考勤打卡记录的起始工作日，格式为“yyyy-MM-dd HH:mm:ss*/
        request.setWorkDateFrom(startTime);

        /**查询考勤打卡记录的起始工作日，格式为“yyyy-MM-dd HH:mm:ss,起始与结束工作日最多相隔7天*/
        request.setWorkDateTo(endTime);

        /**员工在企业内的UserID列表,最多不能超过50个*/
        request.setUserIdList(useridList);

        /**表示获取考勤数据的起始点，示例值：0L*/
        request.setOffset(offset);

        /**表示获取考勤数据的条数，最大不能超过50条，示例值：10L*/
        request.setLimit(limit);

        OapiAttendanceListResponse response = null;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            log.error(">>Msg.", e);
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 查询用户的请假状态
     * @param useridList 待查询用户id列表，支持最多100个用户的批量查询
     * @param startTime 开始时间 ，UNIX时间戳，支持最多180天的查询
     * @param endTime   结束时间 ，UNIX时间戳，支持最多180天的查询时间
     * @param offset 分页偏移，非负整数
     * @param size   分页大小，正整数，最大20
     * @return
     */
    public static OapiAttendanceGetleavestatusResponse getLeaveStatus(String startTime,String endTime,List<String> useridList,
                                                                      Long offset, Long size) throws ParseException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavestatus");

        OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();

        /**待查询用户id列表，支持最多100个用户的批量查询*/
        req.setUseridList(StringUtils.join(useridList,","));

        /**获取开始日期转换为Unix时间戳*/
        Long start = df.parse(startTime).getTime();

        /**获取结束日期转换为Unix时间戳*/
        Long end   = df.parse(endTime).getTime();

        /**开始时间 ，UNIX时间戳，支持最多180天的查询*/
        req.setStartTime(start);

        /**结束时间 ，UNIX时间戳，支持最多180天的查询时间*/
        req.setEndTime(end);

        /**分页偏移，非负整数，示例值：0L*/
        req.setOffset(offset);

        /**分页偏移，非负整数,示例值：10L，最大20*/
        req.setSize(size);

        OapiAttendanceGetleavestatusResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
        } catch (ApiException e) {
            log.error(">>Msg.", e);
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());

        return rsp;
    }

    /**
     * 获取角色列表
     * @param offset 分页偏移，默认值：0
     * @param size 分页大小，默认值：20，最大值200
     * @return
     */
    public static OapiRoleListResponse getRoleList(Long offset, Long size){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/list");

        OapiRoleListRequest request = new OapiRoleListRequest();

        request.setOffset(offset);

        request.setSize(size);

        OapiRoleListResponse response = null;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            log.error(">>Msg.", e);
        }

        return response;
    }

    /**
     * 获取角色下的员工列表
     * @param role_id 角色ID
     * @param offset 分页偏移，默认值：0
     * @param size 分页大小，默认值：20，最大值200
     * @return
     */
    public static OapiRoleSimplelistResponse getRoleSimplelist(Long role_id,Long offset, Long size) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/simplelist");

        OapiRoleSimplelistRequest request = new OapiRoleSimplelistRequest();

        request.setRoleId(role_id);

        request.setOffset(offset);

        request.setSize(size);

        OapiRoleSimplelistResponse resp = null;
        try {
            resp = client.execute(request, accessToken);

        } catch (ApiException e) {
            log.error(">>Msg.", e);
        }
        return resp;
    }

    /**
     * 获取角色组
     * @param group_id 角色组的Id
     * @return
     */
    public static OapiRoleGetrolegroupResponse getRoleroup(Long group_id) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/getrolegroup");

        OapiRoleGetrolegroupRequest request = new OapiRoleGetrolegroupRequest();

        request.setGroupId(group_id);

        OapiRoleGetrolegroupResponse response = null;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            log.error(">>Msg.", e);
        }
        return response;
    }

    /**
     *获取角色详情
     * @param roleid 角色Id
     * @return
     */
    public static OapiRoleGetroleResponse getRoleGetrole(Long roleid){

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/role/getrole");

        OapiRoleGetroleRequest req = new OapiRoleGetroleRequest();

        req.setRoleId(roleid);

        OapiRoleGetroleResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
        } catch (ApiException e) {
            log.error(">>Msg.", e);
        }

        return rsp;
    }

    /**
     * 根据Unix时间戳得到时间,转换为日期
     * @param time Unix时间戳
     * @return
     */
    public static String getDates(Long time){

        //将指定时间(毫秒值)传给Date构造器，自动转换为日期，后面记得加上L
        Date date = new Date(time);

        return df.format(date);
    }

    /**
     * 根据Unix时间戳得到时间,转换为日期
     * @param time Unix时间戳
     * @return
     */
    public static String getMHd(Long time){

        //将指定时间(毫秒值)传给Date构造器，自动转换为日期，后面记得加上L
        Date date = new Date(time);

        return dff.format(date);
    }

    /**
     * 获取两个时间的间隔天数
     *@param startTime
     *@param endTime
     * @return
     */
    public static int getCountDay(String startTime,String endTime){

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;

        Date date2 = null;
        try {
            date1 = format1.parse(startTime);

            date2 = format2.parse(endTime);
        }catch (Exception e){
            log.error(">>Msg.", e);
            e.printStackTrace();
        }
        Calendar calendar1 = Calendar.getInstance();

        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();

        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);

        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

        int year1 = calendar1.get(Calendar.YEAR);

        int year2 = calendar2.get(Calendar.YEAR);

        /**不同年*/
        if(year1 != year2){
            int timeDistance = 0;
            for (int i = year1; i<year2;i++){
                /**闰年*/
                if(i%4==0 && i%100 != 0||i%400==0){
                    timeDistance += 366;
                }else{
                    /**不是闰年*/
                    timeDistance +=365;
                }
            }
            return  timeDistance + (day2-day1);
        }else {
            /**同年*/
            return day2-day1;
        }
    }

    public static void main(String[] args) throws ParseException {
        /**开始时间*/
       String startTime = "2019-3-16 00:00:00";

        /**结束时间*/
       String endTime = "2019-6-19 00:00:00";

        /**员工在企业内的UserID列表*/
        List<String> useridList = new ArrayList<>();

        useridList.add("0616502639763892");

        useridList.add("15513183335988721");

        /**查询起点，类型Long*/
        Long offset = 0L;
        /**查询条数，类型Long*/
        Long limit  = 10L;

        /**查询条数，类型Long*/
        int count = getCountDay(startTime,endTime);

        /**
         * 打卡记录，起始与结束工作日最多相隔7天
         * */
        if(count <= 7){

            /**获取打卡结果,根据返回的result判断用户的状态*/
            /**limit表示获取考勤数据的条数，最大不能超过50条*/
            OapiAttendanceListResponse response = getAttendanceList(startTime,endTime,useridList,offset,limit);

            /**获取打卡记录*/
            List<OapiAttendanceListResponse.Recordresult> attendanceList = null;
            if(Objects.equals(0L,response.getErrcode())){
                attendanceList = response.getRecordresult();
            }
            /**打卡记录不为空*/
            if(Objects.nonNull(attendanceList)&&attendanceList.size()>0){
                /**遍历打卡记录*/
                for (OapiAttendanceListResponse.Recordresult r : attendanceList) {

                    /** 时间结果Normal：正常;Early：早退;Late：迟到;SeriousLate：严重迟到；Absenteeism：旷工迟到；NotSigned：未打卡*/
                    String timeResult = r.getTimeResult();

                    /**用户userId*/
                    String userId = r.getUserId();

                    /**获得userId的用户名*/
                    String name = DingDingUtil.getUserinfo(userId,accessToken).getName();

                    /**实际打卡时间*/
                    String userCheckTime = getDates(r.getUserCheckTime().getTime());

                    switch (timeResult){
                        case "Normal":
                            System.out.println(name+"时间"+userCheckTime+"打卡正常");
                            break;
                        case "Early":
                            System.out.println(name+"时间"+userCheckTime+"打卡早退");
                            break;
                        case "Late":
                            System.out.println(name+"时间"+userCheckTime+"打卡迟到");
                            break;
                        case "SeriousLate":
                            System.out.println(name+"时间"+userCheckTime+"打卡严重迟到");
                            break;
                        case "Absenteeism":
                            System.out.println(name+"时间"+userCheckTime+"打卡旷工迟到");
                            break;
                        case "NotSigned":
                            System.out.println(name+"时间"+userCheckTime+"未打卡");
                            break;
                        default:
                            System.out.println("没有该记录");
                    }

                }
            }

            System.out.println(response);
        }


        /**
         * 查询用户的请假状态,支持最多180天的查询时间
         * */
        if(count<=180){

            /**查询用户的请假状态*/
            OapiAttendanceGetleavestatusResponse resp = getLeaveStatus(startTime,endTime,useridList,offset,limit);

            if(Objects.equals(0L,resp.getErrcode())){
                JSONObject json = JSONObject.parseObject(resp.getBody());

                JSONObject jsons = (JSONObject) json.get("result");

                /**获取请假状态列表*/
                JSONArray array = JSONArray.fromObject(jsons.get("leave_status"));
                List<AttendanceGetleave> attenlist = JSONArray.toList(array,AttendanceGetleave.class);

                if(Objects.nonNull(attenlist)&&attenlist.size()>0){

                    /**遍历请假状态列表*/
                    for (AttendanceGetleave a : attenlist) {

                        /**请假单位：“percent_day”表示天，“percent_hour”表示小时*/
                        String duration_unit = a.getDuration_unit();

                        String unit = "";
                        if("percent_day".equals(duration_unit)){
                            unit = "天";
                        }else if("percent_hour".equals(duration_unit)){
                            unit = "小时";
                        }

                        /**假期时长*100，例如用户请假时长为1天，该值就等于100*/
                        Long duration_percent = a.getDuration_percent()/100;

                        /**请假开始时间*/
                        String start_time = getMHd(a.getStart_time());

                        /**请假结束时间*/
                        String end_time = getMHd(a.getEnd_time());

                        /**获得userId的用户名*/
                        String name =  DingDingUtil.getUserinfo(a.getUserid(),accessToken).getName();

                        System.out.println(name+"在时间"+start_time+"至"+end_time+"请假了"+duration_percent+unit);
                    }
                }
            }

        }


        /**
         * 通过父部门id获取子部门列表
         */
        OapiDepartmentListResponse depart = DingDingUtil.getDepartmentList("1",accessToken,false);

        if(Objects.equals(0L,depart.getErrcode())){

          /**通过父部门id获取子部门列表集合*/
          List<OapiDepartmentListResponse.Department>  departList = depart.getDepartment();

            /**遍历部门集合*/
          if(Objects.nonNull(departList)&&departList.size()>0){

              for (OapiDepartmentListResponse.Department d : departList) {

                  /**部门id*/
                  Long id = d.getId();

                  /**部门名称*/
                  String name = d.getName();

                  /**父部门id，根部门为1*/
                  Long parentid = d.getParentid();

                  /**是否同步创建一个关联此部门的企业群，true表示是，false表示不是*/
                  Boolean createDeptGroup = d.getCreateDeptGroup();

                  /**当群已经创建后，是否有新人加入部门会自动加入该群, true表示是，false表示不是*/
                  Boolean autoAddUser = d.getAutoAddUser();

                  System.out.println("部门id为："+id+",部门名称："+name+",父部门id"+parentid+",是否同步创建一个关联此部门的企业群"+createDeptGroup);
              }
          }
        }

        System.out.println(depart);


        /**
         * 通过用户userid获取用户详情
         * */
        String userid = "0616502639763892";

        OapiUserGetResponse getUser = DingDingUtil.getUserinfo(userid,accessToken);

        /**员工名字*/
        String name = getUser.getName();

        /**成员所属部门id列表*/
        List<Long> departList = getUser.getDepartment();

        /**职位信息*/
        String position = getUser.getPosition();

        /**头像url*/
        String avatar = getUser.getAvatar();

        /**手机号码*/
        String mobile = getUser.getMobile();

        /**是否是高管*/
        Boolean isSenior = getUser.getIsSenior();

        /**用户所在角色列表*/
        List<OapiUserGetResponse.Roles> userRoleslist = getUser.getRoles();

        if(Objects.nonNull(userRoleslist)&&userRoleslist.size()>0){

            for (OapiUserGetResponse.Roles roles : userRoleslist) {

                /**用户角色id*/
                Long id = roles.getId();

                System.out.println("用户角色id"+id);

                /**用户角色名称*/
                String rolesName = roles.getName();

                System.out.println("角色名称"+rolesName);

                /**用户角色组名称*/
                String groupName = roles.getGroupName();

                System.out.println("用户角色组名称"+groupName);
            }
        }

        System.out.println(getUser);
        log.info("user",getUser);

        /**
         * 获取角色列表
         * limit 分页大小，默认值：20，最大值200
         * offset 分页偏移，默认值：0
         * */
        OapiRoleListResponse respRole = getRoleList(offset, limit);

        if(Objects.equals(0L,respRole.getErrcode())){

            /**获取角色组及角色列表*/
            List<OapiRoleListResponse.OpenRoleGroup> roleList = respRole.getResult().getList();

            if(Objects.nonNull(userRoleslist)&&userRoleslist.size()>0){

                for (OapiRoleListResponse.OpenRoleGroup o : roleList) {

                    /**角色组名称*/
                    String names  = o.getName();

                    System.out.println("角色组名称"+names);

                    /**角色组id*/
                    Long  groupId = o.getGroupId();

                    System.out.println("角色组id"+groupId);

                    /**该角色组id下角色列表*/
                   List<OapiRoleListResponse.OpenRole> rolexqList  = o.getRoles();

                    if(Objects.nonNull(rolexqList)&&rolexqList.size()>0){

                        for (OapiRoleListResponse.OpenRole openRole : rolexqList) {

                            /**用户角色id*/
                            Long id = openRole.getId();

                            System.out.println("用户角色id"+id);

                            /**用户角色名称*/
                            String roleName = openRole.getName();

                            System.out.println("用户角色名称"+roleName);
                        }
                    }
                }
            }
        }

        System.out.println(respRole);

        log.info("roleList",respRole);

        /**
         * 获取角色下的员工列表
         * limit 分页大小，默认值：20，最大值200
         * offset 分页偏移，默认值：0
         * */
        OapiRoleSimplelistResponse roleSimplelist =  getRoleSimplelist(respRole.getResult().getList().get(0).getRoles().get(0).getId(),offset, limit);

        if(Objects.equals(0L,roleSimplelist.getErrcode())){

            /**获取角色下的员工列表集合*/
            List<OapiRoleSimplelistResponse.OpenEmpSimple> personList = roleSimplelist.getResult().getList();

            if(Objects.nonNull(personList)&&personList.size()>0){

                for (OapiRoleSimplelistResponse.OpenEmpSimple op : personList) {
                    /**员工id*/
                    String userID = op.getUserid();

                    System.out.println("员工id"+userID);

                    /**员工姓名*/
                    String username = op.getName();

                    System.out.println("员工姓名"+username);
                }
            }
        }
        System.out.println(roleSimplelist);



        /**
         * 获取角色组
         * 传入group_id
         * */
        OapiRoleGetrolegroupResponse roleGroup =  getRoleroup(respRole.getResult().getList().get(0).getGroupId());

        if(Objects.equals(0L,roleGroup.getErrcode())){
            /**该角色组id下角色列表*/
            List<OapiRoleGetrolegroupResponse.OpenRole> groupList = roleGroup.getRoleGroup().getRoles();

            if(Objects.nonNull(groupList)&&groupList.size()>0){

                if(Objects.nonNull(groupList)&&groupList.size()>0){

                    for (OapiRoleGetrolegroupResponse.OpenRole ol : groupList) {

                        /**用户角色id*/
                        Long ids = ol.getRoleId();

                        System.out.println("用户角色id"+ids);

                        /**用户角色名称*/
                        String roleNames = ol.getRoleName();

                        System.out.println("用户角色名称"+roleNames);
                    }
                }
            }
        }
        System.out.println(roleGroup);


        /**
         * 获取角色详情
         * 传入roleId
         * */
        /**角色信息*/
        OapiRoleGetroleResponse roleGetrole =  getRoleGetrole(respRole.getResult().getList().get(0).getRoles().get(0).getId());

        if(Objects.equals(0L,roleGroup.getErrcode())){

            /**角色名*/
            String namexq =  roleGetrole.getRole().getName();

            System.out.println("角色名"+namexq);

            /**角色组id*/
            Long groupIdxq = roleGetrole.getRole().getGroupId();

            System.out.println("角色组id"+groupIdxq);
        }
        System.out.println(roleGetrole);

    }
}
