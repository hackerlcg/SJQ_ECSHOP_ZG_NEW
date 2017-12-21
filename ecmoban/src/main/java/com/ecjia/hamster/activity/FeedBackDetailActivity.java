package com.ecjia.hamster.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.FeedBackModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.hamster.adapter.FeedBackMessageSql;
import com.ecjia.hamster.adapter.FeedBackMessagesAdapter;
import com.ecjia.hamster.adapter.FeedBackSql;
import com.ecjia.hamster.model.FEEDBACK_MESSAGE;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/5.
 * 咨询详情页面
 */
public class FeedBackDetailActivity extends BaseActivity implements HttpResponse, TextWatcher, View.OnClickListener, XListView.IXListViewListener {
    private final String TYPE1 = "orders";//订单咨询
    private final String TYPE2 = "goods";//商品咨询
    private final String TYPE3 = "user";
    private final String TYPE4 = "anonymity";
    private String getType = "user";
    private String feedback_id="";
    private FeedBackModel feedBackModel;

    private TextView title_text;
    private ImageView back;
    private XListView feedback_list;
    private TextView close_keyboard;
    private EditText feedback_edit;
    private TextView feedback_send;
    //headView
    private View goods_headView, order_headView;
    private LinearLayout order_headView_item, goods_headView_item;
    private TextView orderNumber, orderPrice, orderTime;
    private ImageView orderImg;

    private TextView goodsTitle, goodsPrice, goodsUrl;
    private ImageView goodsImg;
    private MyBitmapUtils myBitmapUtils;
    int size = 0;
    private FeedBackMessagesAdapter feedBackMessagesAdapter;
    private String uid;
    String contString;
    private LinearLayout bottom_view;
    private ArrayList<FEEDBACK_MESSAGE> Local_messages = new ArrayList<FEEDBACK_MESSAGE>();
    private int page = 1, total_count = 0;
    private FeedBackMessageSql feedBackMessageSql;
    private FeedBackSql feedBackSql;
    private PAGINATED paginated;
    private int position=0;
    private LinearLayout null_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_feed_back_message);
        EventBus.getDefault().register(this);
        initView();
    }


    private void initView() {
        getType = getIntent().getStringExtra("type");
        feedback_id=getIntent().getStringExtra("feedback_id");
        position=getIntent().getIntExtra("position",0);
        feedBackMessageSql = FeedBackMessageSql.getIntence(this);
        feedBackSql = FeedBackSql.getIntence(this);
        uid = SESSION.getInstance().getUid();
        myBitmapUtils = MyBitmapUtils.getInstance(this);
        feedBackModel = new FeedBackModel(this);
        feedBackModel.addResponseListener(this);
        feedBackModel.getFeedBackMessage(getType,feedback_id);

        title_text = (TextView) findViewById(R.id.top_view_text);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
                finish();
            }
        });
        bottom_view = (LinearLayout) findViewById(R.id.bottom_view);
        feedback_list = (XListView) findViewById(R.id.feedback_list);
        feedback_list.setTopPullLoadMore(true);
        feedback_list.setXListViewListener(this, 1);
        feedback_list.setPullLoadEnable(false);
        feedback_list.setPullRefreshEnable(false);
        null_page= (LinearLayout)findViewById(R.id.null_page);
        null_page.setVisibility(View.GONE);
        feedBackMessagesAdapter = new FeedBackMessagesAdapter(this, Local_messages);
        feedback_list.setAdapter(feedBackMessagesAdapter);
        close_keyboard = (TextView) findViewById(R.id.feedback_close_keyboard);
        feedback_edit = (EditText) findViewById(R.id.feedback_edit);
        feedback_send = (TextView) findViewById(R.id.feedback_send);
        if (TYPE1.equals(getType)) {//订单咨询
            initOrderHeadView();
            feedback_list.addHeaderView(order_headView);
        } else if (TYPE2.equals(getType)) {//商品咨询
            initGoodHeadView();
            feedback_list.addHeaderView(goods_headView);
        } else if (TYPE3.equals(getType)) {//会员咨询

        } else if (TYPE4.equals(getType)) {//匿名会员咨询

        } else {
            getType = TYPE3;
        }
        feedback_edit = (EditText) findViewById(R.id.feedback_edit);
        feedback_edit.addTextChangedListener(this);
        feedback_edit.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    close_keyboard.setVisibility(View.VISIBLE);
                    close_keyboard.setEnabled(true);
                } else {
                    close_keyboard.setVisibility(View.GONE);
                    close_keyboard.setEnabled(false);
                }
            }
        });
        feedback_send = (TextView) findViewById(R.id.feedback_send);
        feedback_send.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.feedback_edit:
                break;
            case R.id.feedback_send:
                send();
                break;
            case R.id.top_view_back:
                closeKeyBoard();
                finish();
                break;
            case R.id.feedback_close_keyboard:
                closeKeyBoard();
                break;
        }
    }

    private void initOrderHeadView() {
        //订单头布局
        order_headView = LayoutInflater.from(this).inflate(R.layout.head_feedback_order, null);
        order_headView_item = (LinearLayout) order_headView.findViewById(R.id.feedback_order);
        orderNumber = (TextView) order_headView.findViewById(R.id.feedback_order_number);
        orderPrice = (TextView) order_headView.findViewById(R.id.order_state);
        orderTime = (TextView) order_headView.findViewById(R.id.feedback_order_time);
        orderImg = (ImageView) order_headView.findViewById(R.id.feedback_order_img);


    }

    private void initGoodHeadView() {
        //商品头布局
        goods_headView = LayoutInflater.from(this).inflate(R.layout.head_feedback_goods, null);
        goods_headView_item = (LinearLayout) goods_headView.findViewById(R.id.feedback_goods);
        goodsTitle = (TextView) goods_headView.findViewById(R.id.feedback_goods_title);
        goodsPrice = (TextView) goods_headView.findViewById(R.id.feedback_goods_price);
        goodsUrl = (TextView) goods_headView.findViewById(R.id.feedback_goods_sendurl);
        goodsImg = (ImageView) goods_headView.findViewById(R.id.feedback_goods_img);
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ADMIN_FEEDBACK_MESSAGES) {
            if (status.getSucceed() == 1) {
                if (TextUtils.isEmpty(title_text.getText().toString())) {
                    feedBackMessagesAdapter.setImg_url(feedBackModel.user_item.getAvatar_img());
                    setHeadInfo();
                }
                paginated=feedBackModel.paginated;
                InsertMessage();//向数据库中插入数据
                total_count = feedBackMessageSql.getFeedBackListCountbyUser(getType,feedBackModel.user_item.getUser_id());
                if(Local_messages.size()==0){
                    initLocalData();//加载本地数据
                }else{
                    LoadMoreLocalData();
                }

                if(feedBackModel.feedback_messages_list.size()==0&&Local_messages.size()==0){
                    feedback_list.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                }

            }else{
                if (feedBackModel.feedback_messages_list.size() == 0) {
                    feedback_list.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                } else {
                    ToastView toastView = new ToastView(this, getResources().getString(R.string.error_network));
                    toastView.show();
                }
            }
        } else if (url == ProtocolConst.ADMIN_FEEDBACK_REPLY) {
            if (status.getSucceed() == 1) {
                UpdateListView();
            } else {
                ToastView toastView = new ToastView(this, status.getError_desc());
                toastView.show();
            }
        }
    }

    //初始化加载本地数据加载
    private void initLocalData() {
        page = 1;
        Local_messages.clear();//初始化
        feedBackMessageSql.getFeedBackMessageListByPage(page, Local_messages, getType, feedBackModel.user_item.getUser_id());
        feedBackMessagesAdapter.setConsultionList(Local_messages);

        feedback_list.setRefreshTime();
        feedback_list.stopRefresh();
        feedback_list.stopLoadMore();
        if (Local_messages.size()<total_count||paginated.getMore()==1) {
            feedback_list.setPullRefreshEnable(true);
        } else {
            feedback_list.setPullRefreshEnable(false);
        }
        feedBackMessagesAdapter.notifyDataSetChanged();
        size = Local_messages.size();
        feedback_list.setSelection(size - 1);
        feedBackSql.UpdateMessageContent(Local_messages.get(Local_messages.size()-1).getContent(),Local_messages.get(Local_messages.size()-1).getFormatted_time(),feedback_id);
        EventBus.getDefault().post(new MyEvent("feedback_refresh",position,Local_messages.get(Local_messages.size()-1).getContent()+"="+Local_messages.get(Local_messages.size()-1).getFormatted_time()));
    }

    private void LoadMoreLocalData() {//加载更多
        feedback_list.stopRefresh();
        feedback_list.stopLoadMore();
        ++page;
        feedBackMessageSql.getFeedBackMessageListByPage(page, Local_messages, getType,feedBackModel.user_item.getUser_id());
        feedBackMessagesAdapter.setConsultionList(Local_messages);

        feedback_list.stopLoadMore();
        if (Local_messages.size()<total_count||paginated.getMore()==1) {
            feedback_list.setPullRefreshEnable(true);
        } else {
            feedback_list.setPullRefreshEnable(false);
        }
        feedBackMessagesAdapter.notifyDataSetChanged();
        feedback_list.setSelection(Local_messages.size()-size);
        size = Local_messages.size();
        LG.e("Local_messages=="+Local_messages.size());
    }

    //向数据库中插入数据
    private void InsertMessage() {
        for (FEEDBACK_MESSAGE feedbackMessage : feedBackModel.feedback_messages_list) {
            feedbackMessage.setUser_id(feedBackModel.user_item.getUser_id());
            feedBackMessageSql.InsertValue(feedbackMessage,getType);
        }
        feedBackMessagesAdapter.notifyDataSetChanged();
    }


    //设置
    private void setHeadInfo() {
        title_text.setText(feedBackModel.user_item.getUser_name());
        if (TYPE2.equals(getType)) {
            goodsTitle.setText(feedBackModel.feed_goods_item.getName());
            goodsPrice.setText(feedBackModel.feed_goods_item.getShop_price());
            goodsUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendUrl();
                }
            });
            myBitmapUtils.displayImage(goodsImg, feedBackModel.feed_goods_item.getPhoto().getSmall());
        } else if (TYPE1.equals(getType)) {
            orderNumber.setText(feedBackModel.feedback_order_item.getOrder_sn());
            orderPrice.setText(feedBackModel.feedback_order_item.getFormatted_order_status());
            orderTime.setText(feedBackModel.feedback_order_item.getFormatted_create_time());
            if (feedBackModel.feedback_order_item.getGoodsArrayList().size() > 0) {
                myBitmapUtils.displayImage(orderImg, feedBackModel.feedback_order_item.getGoodsArrayList().get(0).getImg().getSmall());
            } else {
                orderImg.setBackgroundResource(R.drawable.default_image);
            }
        }

    }


    public void onEvent(MyEvent event) {

    }

    private void UpdateListView() {
        FEEDBACK_MESSAGE feedback_message = new FEEDBACK_MESSAGE();
        feedback_message.setIs_myself(1);
        feedback_message.setContent(contString);
        Date now=new Date();
        feedback_message.setFormatted_time(TimeUtil.format.format(now));
        feedback_message.setTime((now.getTime()/1000) + "");
        Local_messages.add(feedback_message);
        feedBackMessagesAdapter.notifyDataSetChanged();
        feedback_list.setSelection(feedback_list.getCount() - 1);
        feedback_edit.setText("");
        feedBackSql.UpdateMessageContent(Local_messages.get(Local_messages.size() - 1).getContent(), Local_messages.get(Local_messages.size() - 1).getFormatted_time(), feedback_id);
        EventBus.getDefault().post(new MyEvent("feedback_refresh", position, Local_messages.get(Local_messages.size() - 1).getContent() + "=" + Local_messages.get(Local_messages.size() - 1).getFormatted_time()));
    }

    //发送咨询。
    private void send() {

        contString = feedback_edit.getText().toString();
        if (TextUtils.isEmpty(contString.trim())) {
            ToastView toast = new ToastView(this, res.getString(R.string.not_null));
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            feedback_edit.setText("");
        } else {
            feedBackModel.createFeedBackString(getType, contString,feedback_id);
        }
    }


    //发送咨询（链接）。
    private void sendUrl() {
        String goods_id = feedBackModel.feed_goods_item.getId();
        String url = AndroidManager.APPAPI + "/goods.php?id=" + goods_id;
        contString = url;
        feedBackModel.createFeedBackString(getType, url,feedback_id);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (feedback_edit.getText().toString() == null || "".equals(feedback_edit.getText().toString())) {
            feedback_send.setBackgroundResource(0);
            feedback_send.setTextColor(getResources().getColor(R.color.my_dark));
            feedback_send.setEnabled(false);
        } else {
            feedback_send.setBackgroundResource(R.drawable.shape_public_bg);
            feedback_send.setTextColor(Color.parseColor("#ffffffff"));
            feedback_send.setEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (feedback_edit.getText().toString() == null || "".equals(feedback_edit.getText().toString())) {
            feedback_send.setBackgroundResource(0);
            feedback_send.setTextColor(getResources().getColor(R.color.my_dark));
            feedback_send.setEnabled(false);
        } else {
            feedback_send.setBackgroundResource(R.drawable.shape_public_bg);
            feedback_send.setTextColor(Color.parseColor("#ffffffff"));
            feedback_send.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (feedback_edit.getText().toString() == null || "".equals(feedback_edit.getText().toString())) {
            feedback_send.setBackgroundResource(0);
            feedback_send.setTextColor(getResources().getColor(R.color.my_dark));
            feedback_send.setEnabled(false);
        } else {
            feedback_send.setBackgroundResource(R.drawable.shape_public_bg);
            feedback_send.setTextColor(Color.parseColor("#ffffffff"));
            feedback_send.setEnabled(true);
        }
    }

    // 关闭键盘
    public void closeKeyBoard() {
        feedback_edit.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(feedback_edit.getWindowToken(), 0);
    }

    @Override
    public void onRefresh(int id) {
        if(null!=paginated){
            if(paginated.getMore()==1){
                feedBackModel.getFeedBackMessageMore(getType, feedback_id);
            }else{
                LoadMoreLocalData();
            }
        }
    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isInIgnoredView(ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        bottom_view.getGlobalVisibleRect(rect);
        if (rect.contains((int) ev.getX(), (int) ev.getY())) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
