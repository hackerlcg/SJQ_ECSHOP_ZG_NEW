package com.ecjia.hamster.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseFragment;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.ServiceModel;
import com.ecjia.component.view.LeftSlidingListView;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.hamster.adapter.FeedBackSql;
import com.ecjia.hamster.adapter.ServiceFragmentAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SERVICE_MESSAGE;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
/**
 * 类名介绍：客服
 * Created by sun
 * Created time 2017-03-15.
 */
public class ServiceFragment extends BaseFragment implements HttpResponse, XListView.IXListViewListener {
    private ImageView back;
    private TextView top_view_text;
    private View containerView;
    private LeftSlidingListView service_listView;
    private ServiceModel serviceModel;
    private ServiceFragmentAdapter serviceFragmentAdapter;
    //---------头布局----------
    private LinearLayout headView, good_item, order_item,anonymity_item;
    private TextView good_num, order_num,anonymity_num,top_text;
    private FeedBackSql feedBackSql;
    private ArrayList<SERVICE_MESSAGE> Local_messages = new ArrayList<SERVICE_MESSAGE>();
    private int page = 1, total_count = 0;
    private String type="user";
    private PAGINATED paginated;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedBackSql = FeedBackSql.getIntence(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_service, container, false);
        EventBus.getDefault().register(this);
        initView();

        return containerView;
    }

    private void initView() {
        back = (ImageView) containerView.findViewById(R.id.top_view_back);
        back.setVisibility(View.GONE);
        top_view_text = (TextView) containerView.findViewById(R.id.top_view_text);
        top_view_text.setText(getResources().getString(R.string.servicefragment_title));
        initHeadView();
        service_listView = (LeftSlidingListView) containerView.findViewById(R.id.service_listview);
        service_listView.setRightViewWidth((int) getResources().getDimension(R.dimen.dp_80));
        service_listView.setPullLoadEnable(false);
        service_listView.setPullRefreshEnable(true);
        service_listView.setXListViewListener(this, 1);
        service_listView.addHeaderView(headView);
        service_listView.setHeadView(headView);//添加屏蔽头布局
        serviceModel = new ServiceModel(this.getActivity());
        serviceModel.addResponseListener(this);
        serviceModel.getFeedBackList("user");
        if (serviceFragmentAdapter == null) {
            serviceFragmentAdapter = new ServiceFragmentAdapter(getActivity(), serviceModel.messageArrayList);
        }
        serviceFragmentAdapter.setOnRightItemClickListener(new ServiceFragmentAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, final int position) {

                final MyDialog myDialog = new MyDialog(getActivity(),getResources().getString(R.string.dialog_title), getResources().getString(R.string.delete_dialog_content));
                myDialog.show();
                myDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedBackSql.DeleteById(Local_messages.get(position).getFeedback_id());
                        Local_messages.remove(position);
                        if(Local_messages.size()==0){
                            top_text.setVisibility(View.GONE);
                        }else{
                            top_text.setVisibility(View.VISIBLE);
                        }
                        service_listView.hiddenRight(service_listView.mCurrentItemView);
                        serviceFragmentAdapter.notifyDataSetChanged();
                        myDialog.dismiss();
                    }
                });

            }
        });

        service_listView.setAdapter(serviceFragmentAdapter);

    }

    private void initHeadView() {
        headView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.service_head_view, null);
        good_item = (LinearLayout) headView.findViewById(R.id.good_item);
        good_num = (TextView) headView.findViewById(R.id.good_num);
        order_item = (LinearLayout) headView.findViewById(R.id.order_item);
        order_num = (TextView) headView.findViewById(R.id.order_num);
        anonymity_item= (LinearLayout) headView.findViewById(R.id.anonymity_item);
        anonymity_num= (TextView) headView.findViewById(R.id.anonymity_num);
        top_text=(TextView) headView.findViewById(R.id.top_text);
        good_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabsFragment.getInstance().msgFragment(8, "goods");
            }
        });
        order_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabsFragment.getInstance().msgFragment(8, "orders");
            }
        });

        anonymity_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabsFragment.getInstance().msgFragment(8, "anonymity");
            }
        });

    }



    //初始化加载本地数据加载
    private void initLocalData() {
        page = 1;
        Local_messages.clear();//初始化
        feedBackSql.getFeedBackListByPage(page, Local_messages, type);
        serviceFragmentAdapter.setService_messages(Local_messages);
        service_listView.setRefreshTime();
        service_listView.stopRefresh();
        service_listView.stopLoadMore();
        if (Local_messages.size()<total_count||paginated.getMore()==1) {
            service_listView.setPullLoadEnable(true);
        } else {
            service_listView.setPullLoadEnable(false);
        }

        serviceFragmentAdapter.notifyDataSetChanged();
    }

    private void LoadMoreLocalData() {//加载更多
        ++page;
        feedBackSql.getFeedBackListByPage(page, Local_messages, type);
        serviceFragmentAdapter.setService_messages(Local_messages);

        service_listView.stopLoadMore();
        if (Local_messages.size()<total_count||paginated.getMore()==1) {
            service_listView.setPullLoadEnable(true);
        } else {
            service_listView.setPullLoadEnable(false);
        }
        serviceFragmentAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ADMIN_FEEDBACK_LIST) {
            if (status.getSucceed() == 1) {
                if(Local_messages.size()==0) {
                    Integer good_nums=Integer.valueOf(serviceModel.serviceinfo.getGoods_messages());
                    Integer order_nums=Integer.valueOf(serviceModel.serviceinfo.getOrders_messages());
                    if(good_nums<=99){
                        if(good_nums>0){
                            good_num.setText(good_nums+"");
                        }else{
                            good_num.setText("");
                        }
                    }else{
                        good_num.setText("99+");
                    }
                    if(order_nums<=99){
                        if(order_nums>0){
                            order_num.setText(order_nums+"");
                        }else {
                            order_num.setText("");
                        }
                    }else{
                        order_num.setText("99+");
                    }

                }
                paginated=serviceModel.paginated;
                InsertMessage();
                total_count = feedBackSql.getAllFeedBackListCount(type);
                if(Local_messages.size()==0){
                    initLocalData();//加载本地数据
                }else{
                    LoadMoreLocalData();
                }

                if(serviceModel.messageArrayList.size()==0&&Local_messages.size()==0){
                    top_text.setVisibility(View.GONE);
                }else{
                    top_text.setVisibility(View.VISIBLE);
                }


            } else {
                if (serviceModel.messageArrayList.size() == 0) {
                    top_text.setVisibility(View.GONE);
                } else {
                    ToastView toastView = new ToastView(getActivity(), getResources().getString(R.string.error_network));
                    toastView.show();
                }
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        Local_messages.clear();
        serviceModel.getFeedBackList(type);
    }

    @Override
    public void onLoadMore(int id) {
        if(null!=paginated){
            if(paginated.getMore()==1){
                serviceModel.getFeedBackListMore(type);
            }else{
                LoadMoreLocalData();
            }
        }
    }

    //向数据库中插入数据
    private void InsertMessage() {
        if(serviceModel.messageArrayList.size()>0) {
            for (SERVICE_MESSAGE service_message : serviceModel.messageArrayList) {
                feedBackSql.InsertMessageData(service_message, type);
            }
            serviceFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }


    public void onEvent(MyEvent event) {
        if ("feedback_refresh".equals(event.getMsg())) {
            String[] strs=event.getContent().split("=");
            Local_messages.get(event.getmTag()).setContent(strs[0]);
            Local_messages.get(event.getmTag()).setFormatted_time(strs[1]);

            serviceFragmentAdapter.notifyDataSetChanged();
        }

    }
}
