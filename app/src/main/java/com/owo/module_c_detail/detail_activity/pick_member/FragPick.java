package com.owo.module_c_detail.detail_activity.pick_member;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.owo.base.FragBase;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanUser;

import com.owo.module_c_detail.AtyDetail;
import com.owo.module_c_detail.detail_activity.FragDetailActivity;
import com.owo.utils.Common;
import com.owo.utils.MapCalculator;
import com.owo.utils.util_http.HttpHelper;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
import com.wao.dogcat.widget.CircleImageView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.application.JChatDemoApplication;
import io.jchat.android.chatting.ChatActivity;
import io.jchat.android.chatting.utils.HandleResponseCode;

/**
 * 筛选
 * @author XQF
 * @created 2017/5/23
 */
public class FragPick extends FragBase implements ViewPick {


    public static FragPick newInstance(String taskName1,int taskID1,int maxNum1,int takenNum1) {
        FragPick fragPick = new FragPick();
        taskName = taskName1;
        taskID = taskID1;
        maxNum = maxNum1;
        taskTakenNum = takenNum1;
        return fragPick;
    }

    public static int taskID;
    public static int maxNum;
    public static String taskName;
    public static int taskTakenNum;



    @OnClick(R.id.btnBack)
    public void back(){
        getActivity().finish();
    }


    private DisplayImageOptions options1;
    private String path1;
    private int userID;


    private PresenterPick presenterPick;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.noData)
    TextView noData;

    @BindView(R.id.pick_btn)
    Button button;

    @BindView(R.id.recyclerview_pick)
    RecyclerView recyclerView;



    private int code;

    private List<BeanUser> userList;

    AdapterRecyclerView adapter;

    private String str = "";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_offline_activity_pick, container, false);
        ButterKnife.bind(this, view);
        Common.userSP = getContext().getSharedPreferences("userSP", 0);
        userID = Common.userSP.getInt("ID",0);
        presenterPick = new PresenterPickImpl(this);
        presenterPick.getApplyUsersByTID(taskID);
        userList = new ArrayList<>();
        return view;
    }




    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (userList.size()>0) {
                        loading.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new AdapterRecyclerView(getActivity());
                        adapter.addItems(userList);
                        recyclerView.setAdapter(adapter);

                    }else{
                        loading.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("暂时没有可筛选的用户");
                    }

                    break;
            }
        }
    };




    @OnClick(R.id.pick_btn)
    public void click(){

//        adapter.updateDataSet(adapter.getSelectedItem());
//        adapter.notifyDataSetChanged();

        int code = setApplyUsersTaskState();
        if (code == 200) {

            try {
            HashMap<String, String> map = new HashMap<>();
            map.put("taskID", taskID + "");
            map.put("taskTakenNum",(taskTakenNum+adapter.getSelectedItem().size())+"");
            String json = HttpHelper.postData(MyURL.UPDATE_TASK_TAKEN_NUM_BY_ID, map, null);
            int code1 = HttpHelper.getCode(json);

            if (code1==200) {
                Common.display(getContext(), "操作成功");
                getActivity().finish();
                String[] users = str.split("_");
                for (int i = 0; i < users.length; i++) {
                    JMessageClient.createSingleTextMessage("yup_" + users[i], "4d454221295c35af705cc26e", "你申请的活动【" + taskName + "】已通过我的审核\n(*￣︶￣)");
                }


//            JMessageClient.createGroup(taskName, "该活动由发起者创建", new CreateGroupCallback() {
//
//                @Override
//                public void gotResult(final int status, String msg, final long groupId) {
//                    if (status == 0) {
//                        Conversation conv = Conversation.createGroupConversation(groupId);
//                        String [] users = str.split("_");
//                        List<String> userNameList = new ArrayList<String>();
//                        for (int i = 0;i<users.length;i++){
//                         userNameList.add("yup_"+users[i]);
//                        }
//
//                        JMessageClient.addGroupMembers(groupId,"4d454221295c35af705cc26e",userNameList,new BasicCallback(){
//
//                            @Override
//                            public void gotResult(int i, String s) {
//                                if (i==0){
//                                    JMessageClient.createGroupTextMessage(groupId,"你申请的活动【"+taskName+"】已通过我的审核(*￣︶￣)");
//                                }else{
//                                    HandleResponseCode.onHandle(getActivity(), i, false);
//                                    Log.i("CreateGroupController", "status : " + i);
//                                }
//
//                            }
//                        });
//
//                    } else {
//                        HandleResponseCode.onHandle(getActivity(), status, false);
//                        Log.i("CreateGroupController", "status : " + status);
//                    }
//                }
//            });
            }else {
                Common.display(getContext(),"操作失败："+code1);
            }

            }catch (Exception e){
                e.printStackTrace();
                Common.display(getContext(),"操作失败：Exception");
            }

        }else if (code == -1){
            Common.display(getContext(),"请至少选择一个用户");
        }else if (code == 201){
            Common.display(getContext(),"已超过人数限制");
        }
         else  Common.display(getContext(),"操作失败：CODE "+code);

    }

    public int setApplyUsersTaskState() {
        int code = 0;
        if (adapter.getSelectedItem().size()>0) {
            try {

                for (int i = 0; i < adapter.getSelectedItem().size(); i++) {
                    if (i != adapter.getSelectedItem().size() - 1)
                        str += adapter.getSelectedItem().get(i).getId() + "_";
                    else str += adapter.getSelectedItem().get(i).getId();
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("userIDs ", str);
                map.put("taskID", taskID + "");
                map.put("applyStatus","2");
                String json = HttpHelper.postData(MyURL.SET_APPLY_USERS_TASK_STATE, map, null);
                code = HttpHelper.getCode(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else code = -1;

        return code;

    }




    @Override
    public void getApplyUsers(List<BeanUser> userLists) {
        this.userList = userLists;
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

    }

    public class AdapterRecyclerView extends RecyclerView.Adapter<Holder> {

        List<BeanUser> list;
        Context mContext;


        private int count;
        private int lastAnimatedPosition=-1;
        private boolean animationsLocked = false;
        private boolean delayEnterAnimation = true;

        private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
        private boolean mIsSelectable = false;

        public AdapterRecyclerView(Context context) {
            mContext = context;
            list = new ArrayList<>();
        }

        //更新adpter的数据和选择状态
        public void updateDataSet(ArrayList<BeanUser> list) {
            this.list = list;
            mSelectedPositions = new SparseBooleanArray();
        }

        //获得选中条目的结果
        public ArrayList<BeanUser> getSelectedItem() {
            ArrayList<BeanUser> selectList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (isItemChecked(i)) {
                    selectList.add(list.get(i));
                }
            }
            return selectList;
        }

        //设置给定位置条目的选择状态
        private void setItemChecked(int position, boolean isChecked) {
            mSelectedPositions.put(position, isChecked);
        }

        //根据位置判断条目是否选中
        private boolean isItemChecked(int position) {
            return mSelectedPositions.get(position);
        }

        //根据位置判断条目是否可选
        private boolean isSelectable() {
            return mIsSelectable;
        }
        //设置给定位置条目的可选与否的状态
        private void setSelectable(boolean selectable) {
            mIsSelectable = selectable;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.frag_detail_pick_item, parent, false);
            return new Holder(mContext, view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, final int position) {
            runEnterAnimation(holder.itemView,position);
            final BeanUser item = list.get(position);
            holder.bind(item);

               holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isItemChecked(position)) {
                        holder.btn.setBackgroundResource(R.drawable.btn_capsule_empty_yellow);
                        holder.btn.setTextColor(mContext.getResources().getColor(R.color.yupColor));
                        setItemChecked(position, false);
                    } else {
                        holder.btn.setBackgroundResource(R.drawable.btn_capsule_yellow);
                        holder.btn.setTextColor(mContext.getResources().getColor(R.color.white));
                        setItemChecked(position, true);
                    }
                    button.setText("确定人员 ("+getSelectedItem().size()+"/"+maxNum+")");
                    FragDetailActivity.mBeanTask.setTaskTakenNum(FragDetailActivity.mBeanTask.getTaskTakenNum()+getSelectedItem().size());
                }
            });

        }


        private void runEnterAnimation(View view, int position) {
            if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画
            if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
                lastAnimatedPosition = position;
                view.setTranslationY(500);//相对于原始位置下方500
                view.setAlpha(0.f);//完全透明
                //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
                //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
                view.animate()
                        .translationY(0).alpha(1.f)//设置最终效果为完全不透明，并且在原来的位置
                        .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                        .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                        .setDuration(400)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                            }
                        })
                        .start();
            }
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public void addItems(List<BeanUser> list) {
            this.list = list;

        }
    }


    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.pick_item_avatar)
        CircleImageView head;

        @BindView(R.id.pick_item_name)
        TextView username;

        @BindView(R.id.pick_item_age)
        TextView age;


        @BindView(R.id.pick_item_dis)
        TextView dis;


        @BindView(R.id.pick_item_sex)
        ImageView sex;

        @BindView(R.id.pick_item_height)
        TextView height;

        @BindView(R.id.pick_item_weight)
        TextView weight;

        @BindView(R.id.pick_item_sign)
        TextView sign;


        @BindView(R.id.pick_item_cardview)
        protected CardView mCardView;

        @BindView(R.id.pick_item_btn)
        protected Button btn;

        private Context mContext;


        private DisplayImageOptions options;
        private String path;


        public Holder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = context;
            // 使用ImageLoader加载网络图片
            options = new DisplayImageOptions.Builder()//
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)//图片大小刚好满足控件尺寸
                    .showImageForEmptyUri(R.drawable.my) //
                    .showImageOnFail(R.drawable.my) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .build();//

            Common.userSP = mContext.getSharedPreferences("userSP", 0);
        }



        public void bind(final BeanUser item) {
            //pickUserID = item.getId();
            username.setText(item.getUserName());
            path = MyURL.ROOT+item.getAvatar();


            //防止图片闪烁
            final String tag = (String)head.getTag();

            if (tag == null || !tag.equals(path)) {
                head.setTag(path);
                com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                        .displayImage(path, head, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                head.setTag(path);//确保下载完成再打tag.
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
            }



            double distance = Integer.MAX_VALUE;
            if (item.getLatitude().trim().length()>0 && item.getLongtitude().trim().length()>0)
                distance = (MapCalculator.getDistance(item.getLatitude(),item.getLongtitude(),
                        Common.myLatitude+"",Common.myLongtitude+"") / 1000);
            String disStr = distance >= 1 ? (  distance>100?">100km": (int) distance + "km") : ("<1km");
            dis.setText(disStr);
            if (item.getSex().equals("2")) {
                sex.setImageResource(R.drawable.frag_home_change_item_wemen);
            }
            age.setText(item.getAge()+"岁");
            height.setText(item.getHeight()+"cm");
            weight.setText(item.getWeight()+"kg");
            sign.setText(item.getSignature());



        }




    }

}
