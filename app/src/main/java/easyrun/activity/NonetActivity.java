package easyrun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import easyrun.util.R;


/**
 * Created by J_Crocodile on 2016/6/10.
 */
public class NonetActivity extends Activity {
    private Button reload1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActionBar().hide();
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决软键盘遮挡
        /*setContentView(R.layout.nonet);

        reload1 = (Button)findViewById(R.id.reload);

*/
    }
}
