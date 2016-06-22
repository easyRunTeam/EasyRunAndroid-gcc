package easyrun.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.ramotion.foldingcell.FoldingCell;
import java.util.HashSet;
import java.util.List;

import easyrun.bean.FreePicBean;
import easyrun.shopping.manager.ImageManager;
import easyrun.util.R;
import easyrun.util.Tools;


public class PictureListAdapter extends ArrayAdapter<FreePicBean> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private ImageManager imageManager;
    public PictureListAdapter(Context context, List<FreePicBean> objects) {
        super(context, 0, objects);
        imageManager=new ImageManager(context);
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
            viewHolder.iconURL=(BootstrapCircleThumbnail)cell.findViewById(R.id.userIMG);
            viewHolder.freePic=(ImageView)cell.findViewById(R.id.freePic);
            viewHolder.eventName1=(TextView)cell.findViewById(R.id.eventName1);
            viewHolder.freePic1=(ImageView)cell.findViewById(R.id.freePic1);
            viewHolder.freePic=(ImageView)cell.findViewById(R.id.freePic);
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
        //System.out.println("\n********" + time.substring(11) + "*******\n");
        //System.out.println("\n********" + time.substring(0, 10) + "*******\n");
        viewHolder.upload_date.setText(time.substring(0, 10));
        viewHolder.downloadTime.setText(Integer.toString(item.getDownloadCnt()));
        viewHolder.eventName.setText(item.getEventName());
        viewHolder.eventName1.setText(item.getEventName());
        //imageManager.loadUrlImage("http://120.27.106.188:8088/Athlete/cutface/3530a0aa5a4743fedbd4b27804c86ea3-0.jpg", viewHolder.iconURL);
        //  System.out.println("http://120.27.106.188:8088/EasyRunServer/UserPic/" + item.getEventName() + "/" + item.getPicID());
        // imageManager.loadUrlImage("http://120.27.106.188:8088/EasyRunServer/UserPic/" + item.getEventName() + "/" + item.getPicID(), viewHolder.freePic1);
        //imageManager.loadResImage(R.drawable.default_free_pic, viewHolder.freePic);
        // imageManager.loadResImage(R.drawable.default_free_pic,viewHolder.freePic1);
        viewHolder.freePic.setImageBitmap(item.getPic());
        viewHolder.freePic1.setImageBitmap(item.getPic());
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
        ImageView freePic;                 //照片
        TextView eventName1;
        ImageView freePic1;
    }
}
