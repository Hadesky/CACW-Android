package com.hadesky.cacw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.ui.activity.TeamInfoActivity;
import com.hadesky.cacw.util.ImageUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 我的团队adapter
 * Created by 45517 on 2016/3/21.
 */
public class MyTeamAdapter extends BaseAdapter<TeamMember> {

    public MyTeamAdapter(List<TeamMember> list, @LayoutRes int layoutid) {
        super(list, layoutid);
    }

    @Override
    public BaseViewHolder<TeamMember> createHolder(final View v, Context context) {

        BaseViewHolder<TeamMember> holder = new BaseViewHolder<TeamMember>(v) {
            @Override
            public void setData(TeamMember teamBean) {
                setTextView(R.id.tv_team_name, teamBean.getTeam().getTeamName());
                setTextView(R.id.tv_team_summary, teamBean.getTeam().getSummary());

                BmobQuery<TeamMember> query = new BmobQuery<>();
                query.addWhereEqualTo("mTeam", new BmobPointer(teamBean.getTeam()));
                query.findObjects(new FindListener<TeamMember>() {
                    @Override
                    public void done(List<TeamMember> list, BmobException e) {
                        if (e == null) {
                            View view = findView(R.id.layout_member_count);
                            view.setVisibility(View.VISIBLE);
                            setTextView(R.id.tv_member_count, "" + list.size());
                        }
                    }
                });
                if (teamBean.getTeam().getTeamAvatar() != null) {
                    SimpleDraweeView view = findView(R.id.sdv_team_icon);
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(teamBean.getTeam().getTeamAvatar().getUrl()))
                            .setPostprocessor(new PaletteProcessor((CardView) findView(R.id.card_view)))
                            .build();

                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(view.getController())
                            .build();
                    view.setController(controller);
                }
                Button newProjectBt = findView(R.id.bt_new_project);
            }
        };
        holder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                TeamBean teamBean = mDatas.get(position).getTeam();
                Intent intent = new Intent(mContext, TeamInfoActivity.class);
                intent.putExtra(TeamInfoActivity.IntentTag, teamBean);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    private void handleCardViewBg(CardView cardView, SimpleDraweeView simpleDraweeView) {
        Bitmap bitmap = simpleDraweeView.getDrawingCache();
        int bgColor = ImageUtils.getBitmapLightColor(bitmap);
        cardView.setCardBackgroundColor(bgColor);
    }

    class PaletteProcessor extends BasePostprocessor {
        private CardView mCardView;
        public PaletteProcessor(CardView cardView) {
            mCardView = cardView;
        }

        @Override
        public void process(Bitmap bitmap) {
            if (mCardView != null) {
                int color = ImageUtils.getBitmapLightColor(bitmap);
                mCardView.setCardBackgroundColor(color);
            }
        }
    }
}
