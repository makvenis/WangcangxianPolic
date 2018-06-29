package sc.gys.wcx.and.hotelcheck;

import java.util.List;

/**
 * Created by dell on 2018/4/7.
 */

public class HotelChectEntry {

    private String mTitle; //标题
    private List<String> mList; // 问题及其答案
    private String mText; //备注


    public HotelChectEntry(String mTitle, List<String> mList, String mText) {

        this.mTitle = mTitle;
        this.mList = mList;
        this.mText = mText;
    }

    public HotelChectEntry() {

    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<String> getmList() {
        return mList;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    @Override
    public String toString() {
        return "HotelChectEntry{" +
                "mTitle='" + mTitle + '\'' +
                ", mList=" + mList +
                ", mText='" + mText + '\'' +
                '}';
    }
}
