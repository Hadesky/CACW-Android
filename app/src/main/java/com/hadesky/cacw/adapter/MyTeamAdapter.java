package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.ui.activity.NewTeamActivity;
import com.hadesky.cacw.ui.activity.TeamInfoActivity;
import com.hadesky.cacw.util.ImageUtils;

import java.util.List;

/**
 * 我的团队adapter
 * Created by 45517 on 2016/3/21.
 */
public class MyTeamAdapter extends BaseAdapter<TeamBean>
{
    public static final int TYPE_TEAM = 0;
    public static final int TYPE_NEW_TEAM = 1;

    public MyTeamAdapter(List<TeamBean> list, @LayoutRes int layoutid)
    {
        super(list, layoutid);
    }

    private boolean mCache = false;
//    private Subscription mSubscription;

    @Override
    public BaseViewHolder<TeamBean> onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_NEW_TEAM)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_team, parent, false);
            mContext = parent.getContext();
            return createNewTeamViewHolder(view);
        } else
        {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<TeamBean> holder, int position)
    {
        if (position >= mDatas.size())
        {
            holder.setData(null);
        } else
        {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position == mDatas.size() ? TYPE_NEW_TEAM : TYPE_TEAM;
    }

    private BaseViewHolder<TeamBean> createNewTeamViewHolder(View view)
    {
        return new BaseViewHolder<TeamBean>(view)
        {
            @Override
            public void setData(TeamBean teamMember)
            {
                setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void OnItemClick(View view1, int position)
                    {
                        Intent intent = new Intent(mContext, NewTeamActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }
        };
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size() + 1;
    }

    @Override
    public BaseViewHolder<TeamBean> createHolder(final View v, Context context)
    {

        BaseViewHolder<TeamBean> holder = new BaseViewHolder<TeamBean>(v)
        {
            @Override
            public void setData(TeamBean teamBean)
            {
                setTextView(R.id.tv_team_name, teamBean.getTeamName());
                setTextView(R.id.tv_team_summary, teamBean.getSummary());

                loadMemberCount(teamBean);
                SimpleDraweeView view = findView(R.id.sdv_team_icon);
                if (teamBean.getTeamAvatarUrl() != null)
                {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(teamBean.getTeamAvatarUrl()))
                            //                            .setPostprocessor(new PaletteProcessor((CardView) findView(R.id.card_view)))
                            .build();

                    DraweeController controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(view.getController()).build();
                    view.setController(controller);
                } else
                {
                    view.setImageURI((String) null);
                }
                Button newProjectBt = findView(R.id.bt_new_project);
            }

            private void loadMemberCount(TeamBean teamBean)
            {

            }
        };
        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener()
        {
            @Override
            public void OnItemClick(View view, int position)
            {
                TeamBean teamBean = mDatas.get(position);
                navigateToTeamInfo(teamBean);
            }
        });

        return holder;
    }

    private void navigateToTeamInfo(TeamBean teamBean)
    {
        Intent intent = new Intent(mContext, TeamInfoActivity.class);
        intent.putExtra(TeamInfoActivity.IntentTag, teamBean);
        mContext.startActivity(intent);
    }

    class PaletteProcessor extends BasePostprocessor
    {
        private CardView mCardView;

        public PaletteProcessor(CardView cardView)
        {
            mCardView = cardView;
        }

        @Override
        public void process(Bitmap bitmap)
        {
            if (mCardView != null)
            {
                int color = ImageUtils.getBitmapLightColor(bitmap);
                mCardView.setCardBackgroundColor(color);
            }
        }
    }

    public void onDestroy()
    {

    }
}
