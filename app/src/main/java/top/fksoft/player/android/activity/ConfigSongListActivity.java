package top.fksoft.player.android.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.wildma.pictureselector.PictureSelector;
import jdkUtils.io.FileUtils;
import top.fksoft.player.android.R;
import top.fksoft.player.android.config.SongListBean;
import top.fksoft.player.android.io.BeanIO;
import top.fksoft.player.android.io.FileIO;
import top.fksoft.player.android.utils.android.BitmapUtils;
import top.fksoft.player.android.utils.android.DisplayUtils;
import top.fksoft.player.android.utils.dao.BaseActivity;

import java.io.File;


public class ConfigSongListActivity extends BaseActivity {
    final public static String IntentKey_LIST_ID = "IntentKey_LIST_ID";
    final public static String IntentKey_NEW = "IntentKey_NEW";

    private Toolbar toolbar;
    private TextView saveBtn;
    private TextView deleteBtn;
    private ImageView image;
    private TextView title;
    private Button imgBtn;
    private Button titleBtn;

    private SongListBean listBean = null;

    private String imagePath = null;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra(IntentKey_NEW, false)) {
            listBean = new SongListBean();
            setTitle(R.string.createNewSongList);
            deleteBtn.setVisibility(View.GONE);
            saveBtn.setText(R.string.save);
        } else {
            saveBtn.setText(R.string.edit);
            long sqLiteId = intent.getLongExtra(IntentKey_LIST_ID, -1);
            if (sqLiteId == -1) {
                Toast.makeText(getContext(), R.string.Crack, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                listBean = BeanIO.newInstance().getSongListBean(sqLiteId);
                if (listBean == null) {
                    Toast.makeText(getContext(), R.string.SoftError, Toast.LENGTH_SHORT).show();
                    finish();
                }
                setTitle(getString(R.string.editSongList,listBean.getListName()));
            }
        }
        int px = DisplayUtils.dip2px(getContext(), 60);
        if (listBean.pictureExists()) {//指定歌单的图片
            Bitmap bitmap = BitmapUtils.decodeBitmapFromFile(listBean.getImagePath(), px, px);
            image.setImageBitmap(BitmapUtils.getRoundedCornerBitmap(bitmap, 20));
        } else {
            image.setImageResource(R.drawable.img_draw);
        }
        String listName = listBean.getListName();
        title.setText(listName == null ? "" : listName);//指定默认的标题
    }

    @Override
    protected void initView() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.listTitle);
        image = findViewById(R.id.listArrImg);

        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        imgBtn = findViewById(R.id.imgBtn);
        titleBtn = findViewById(R.id.titleBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        saveBtn.setOnClickListener(this::onClick);
        deleteBtn.setOnClickListener(this::onClick);
        imgBtn.setOnClickListener(this::onClick);
        titleBtn.setOnClickListener(this::onClick);
    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_add_play_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteBtn://删除
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.delete)
                        .setMessage(getString(R.string.delete_message, listBean.getListName()))
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            BeanIO.delete(listBean);
                            finish();
                        }).setNegativeButton(R.string.exit, (dialog, which) -> finish()).show();
                break;
            case R.id.saveBtn://保存响应
                CharSequence text = title.getText();
                if (text.length() == 0) {
                    Toast.makeText(getContext(), R.string.save_error, Toast.LENGTH_SHORT).show();
                    break;
                }
                new AlertDialog.Builder(getContext()).setTitle(saveBtn.getText())
                        .setMessage(getString(R.string.save_message, text))
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            save(text.toString());
                            finish();
                        }).setNegativeButton(R.string.exit, null).show();
                break;
            case R.id.imgBtn://图片点击修改
                PictureSelector
                        .create(getContext(), PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false, 200, 200, 1, 1);
                break;
            case R.id.titleBtn://标题点击修改
                LinearLayout layout = new LinearLayout(getContext());
                layout.setPadding(5, 10, 10, 5);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditText editText = new EditText(getContext());
                editText.setText(title.getText());
                editText.setLayoutParams(layoutParams);
                layout.addView(editText);
                new AlertDialog.Builder(getContext()).setTitle(R.string.edit)
                        .setView(layout)
                        .setPositiveButton(R.string.ok, (dialog, which) -> title.setText(editText.getText())).setNegativeButton(R.string.exit, null).show();
                break;
        }
    }

    private void save(String text) {
        listBean.setListName(text);
        if (imagePath != null && new File(imagePath).isFile()) {
            File file = new File(imagePath);
            String hash = FileUtils.getFileMd5(file);
            File saveFile = new File(FileIO.newInstance().getMusicPicturePath(), hash);
            if (!saveFile.isFile() || saveFile.length() != file.length()) {
                FileUtils.copyFile(file, saveFile);
            }
            listBean.setImageHash(hash);
        }
        BeanIO.save(listBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                int px = DisplayUtils.dip2px(getContext(), 60);
                imagePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                Bitmap bitmap = BitmapUtils.decodeBitmapFromFile(new File(imagePath), px, px);
                Bitmap roundedCornerBitmap = BitmapUtils.getRoundedCornerBitmap(bitmap, 20);
                image.setImageBitmap(roundedCornerBitmap);
            }
        }
    }
}
