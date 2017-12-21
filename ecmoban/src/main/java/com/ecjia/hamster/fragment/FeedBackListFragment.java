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
import com.ecjia.hamster.adapter.FeedBackListAdapter;
import com.ecjia.hamster.adapter.FeedBackSql;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SERVICE_MESSAGE;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/5.
 */
public class FeedBackListFragment extends BaseFragment implements XListView.IXListViewListener, HttpResponse {
    private View view;
    private TextView title_text;
    private ImageView back;
    private LeftSlidingListView service_listview;
    private FeedBackListAdapter feedBackListAdapter;
    private ServiceModel serviceModel;
    private String type;
    private FeedBackSql feedBackSql;
    private ArrayList<SERVICE_MESSAGE> Local_messages = new ArrayList<SERVICE_MESSAGE>();
    private int page = 1, total_count = 0;
    private PAGINATED paginated;
    private LinearLayout null_page;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedBackSql = FeedBackSql.getIntence(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback_list, null);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    private void initView() {
        Local_messages.clear();//清空数据
        type = getArguments().getString("type");
        title_text = (TextView) view.findViewById(R.id.top_view_text);
        if ("orders".equals(type)) {
            title_text.setText(getResources().getString(R.string.consult_order));
        } else if ("goods".equals(type)) {
            title_text.setText(getResources().getString(R.string.consult_goods));
        } else {
            title_text.setText(getResources().getString(R.string.consult));
        }
        back = (ImageView) view.findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TabsFragment.getInstance().toServiceFragment();
            }
        });
        null_page= (LinearLayout) view.findViewById(R.id.null_page);
        null_page.setVisibility(View.GONE);
        service_listview = (LeftSlidingListView) view.findViewById(R.id.feedback_listview);
        service_listview.setRightViewWidth((int) getResources().getDimension(R.dimen.dp_80));
        if (serviceModel == null) {
            serviceModel = new ServiceModel(getActivity());
        } else {
            serviceModel.messageArrayList.clear();
        }
        serviceModel.addResponseListener(this);
        service_listview.setPullLoadEnable(false);
        service_listview.setXListViewListener(this, 1);
        if (feedBackListAdapter == null) {
            feedBackListAdapter = new FeedBackListAdapter(getActivity(), serviceModel.messageArrayList);
        }
        feedBackListAdapter.setOnRightItemClickListener(new FeedBackListAdapter.onRightItemClickListener() {
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
                        service_listview.hiddenRight(service_listview.mCurrentItemView);
                        Local_messages.remove(position);
                        feedBackListAdapter.notifyDataSetChanged();
                        myDialog.dismiss();
                    }
                });

            }
        });
        service_listview.setAdapter(feedBackListAdapter);
        feedBackListAdapter.setType(type);
        serviceModel.getFeedBackList(type);
    }

    //初始化加载本地数据加载
    private void initLocalData() {

        Local_messages.clear();//初始化
        feedBackSql.getFeedBackListByPage(page, Local_messages, type);
        feedBackListAdapter.setService_messages(Local_messages);

        service_listview.setRefreshTime();
        service_listview.stopRefresh();
        service_listview.stopLoadMore();
        if (Local_messages.size()<total_count||paginated.getMore()==1) {
            service_listview.setPullLoadEnable(true);
        } else {
            service_listview.setPullLoadEnable(false);
        }
        feedBackListAdapter.notifyDataSetChanged();
    }

    private void LoadMoreLocalData() {//加载更多

        feedBackSql.getFeedBackListByPage(page, Local_messages, type);
        feedBackListAdapter.setService_messages(Local_messages);
        service_listview.stopLoadMore();
        if (Local_messages.size()<total_count||paginated.getMore()==1) {
            service_listview.setPullLoadEnable(true);
        } else {
            service_listview.setPullLoadEnable(false);
        }
        feedBackListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(int id) {
        page = 1;
        serviceModel.getFeedBackList(type);
    }

    @Override
    public void onLoadMore(int id) {
        ++page;
        if(null!=paginated){
            if(paginated.getMore()==1){
                serviceModel.getFeedBackListMore(type);
            }else{
                LoadMoreLocalData();
            }
        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ADMIN_FEEDBACK_LIST) {
            if (status.getSucceed() == 1) {
                paginated=serviceModel.paginated;
                InsertMessage();
                total_count = feedBackSql.getAllFeedBackListCount(type);
                if(Local_messages.size()==0||page == 1){
                    initLocalData();//加载本地数据
                }else{
                    LoadMoreLocalData();
                }

                if(serviceModel.messageArrayList.size()==0&&Local_messages.size()==0){
                    service_listview.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                }

            } else {
                if (serviceModel.messageArrayList.size() == 0) {
                    service_listview.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                } else {
                    ToastView toastView = new ToastView(getActivity(), getResources().getString(R.string.error_network));
                    toastView.show();
                }
            }
        }
    }

    //向数据库中插入数据
    private void InsertMessage() {
        for (SERVICE_MESSAGE service_message : serviceModel.messageArrayList) {
            feedBackSql.InsertMessageData(service_message,type);
        }
        feedBackListAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
    public void onEvent(MyEvent event) {
        if ("feedback_refresh".equals(event.getMsg())) {
            String[] strs=event.getContent().split("=");
            LG.e("运行"+strs[0]+"---"+strs[1]+"=position="+event.getmTag());
            Local_messages.get(event.getmTag()).setContent(strs[0]);
            Local_messages.get(event.getmTag()).setFormatted_time(strs[1]);
            feedBackListAdapter.notifyDataSetChanged();
        }

    }
}
