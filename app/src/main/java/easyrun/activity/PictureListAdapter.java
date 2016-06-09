package easyrun.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.ramotion.foldingcell.FoldingCell;
import java.util.HashSet;
import java.util.List;

import easyrun.bean.FreePicBean;
import easyrun.util.R;
import easyrun.util.Tools;


public class PictureListAdapter extends ArrayAdapter<FreePicBean> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public PictureListAdapter(Context context, List<FreePicBean> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        FreePicBean item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.picture, parent, false);

            // binding view parts to view holder
            viewHolder.username = (TextView) cell.findViewById(R.id.username);
            viewHolder.upload_time = (TextView) cell.findViewById(R.id.upload_time);
            viewHolder.upload_date = (TextView) cell.findViewById(R.id.upload_date);
            viewHolder.downloadTime = (TextView) cell.findViewById(R.id.downloadTime);
            viewHolder.eventName = (TextView) cell.findViewById(R.id.eventName);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.username.setText(item.getUserName());
        String time = Tools.transDate(item.getUpTime());
        viewHolder.upload_time.setText(time.substring(11));
        System.out.println("\n********" + time.substring(11) + "*******\n");
        System.out.println("\n********" + time.substring(0, 10)+"*******\n");
        viewHolder.upload_date.setText(time.substring(0, 10));
        viewHolder.downloadTime.setText(Integer.toString(item.getDownloadCnt()));
        viewHolder.eventName.setText(item.getEventName());
        return cell;
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        BootstrapCircleThumbnail iconURL;   // 用户头像
        TextView username;                  //用户昵称
        TextView upload_time;               //照片上传时间 几点几分
        TextView upload_date;               //照片上传时间 月/日/年
        TextView downloadTime;              //下载次数
        TextView eventName;                 //赛事名
    }
}
