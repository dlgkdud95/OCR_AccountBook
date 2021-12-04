package com.example.accountbook_uiux;

import static com.example.accountbook_uiux.MainActivity.dbHelper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EconomyViewFragment extends Fragment
{
    private View view_economy;

    public static String TODAY_KOSPI; // 메인엑티비티에서 앱을 실행시키자마자 API호출 및 파싱 후 전역변수로 가져와서 setText
    public static String YSTDAY_KOSPI;
    public static String CHANGE_RATE;

    TextView tv_cardCom, tv_cardCost, tv_cardNum, tv_cardNumbyCom,txt_nickname,tv_kospi;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view_economy = inflater.inflate(R.layout.frame_economy, container, false);

        //tv_cardCom = (TextView) view_economy.findViewById(R.id.tv_cardCom);
        tv_cardCost = (TextView) view_economy.findViewById(R.id.tv_cardCost);
        tv_cardNum = (TextView) view_economy.findViewById(R.id.tv_cardNum);
        tv_cardNumbyCom = (TextView) view_economy.findViewById(R.id.tv_cardNumbyCom);
        //tv_kospi = (TextView) view_economy.findViewById(R.id.tv_kospi);
        txt_nickname = view_economy.findViewById(R.id.txt_nickname);

        String nickname = MainActivity.preferences.getString("name","ㅇㅇㅇ");
        txt_nickname.setText(nickname+"님의\n카드내역");

        String cardNumber = dbHelper.getCardNumber("TRUE");
        String [] array = cardNumber.split("\n");

        String cardCompany = dbHelper.getCardCompany("TRUE");
        String [] array2 = cardCompany.split("\n");

        StringBuilder cardNumberBuilder = new StringBuilder();
        StringBuilder cardCostBuilder = new StringBuilder();
        StringBuilder cardCompanyBuilder = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i <array.length; i++)
        {
            cardNumberBuilder.append(array[i].toString()+"\n");
        }
        for(int i = 0; i < array.length; i++)
        {
            int testINT = dbHelper.getCardCost(array[i]);
            cardCostBuilder.append(Integer.toString(testINT)+"\n");
        }
        for(int i = 0; i < array2.length; i++)
        {
            cardCompanyBuilder.append(array2[i].toString()+"\n");
        }
        for(int i = 0; i < array2.length; i++)
        {
            String card = dbHelper.getCardNumberByCompany(array2[i]);
            stringBuilder.append(card);
        }

        Log.d("카드번호", cardNumberBuilder.toString());
        Log.d("지출금액", cardCostBuilder.toString());
        //Log.d("카드사", cardCompanyBuilder.toString());
        Log.d("카드사           카드번호",stringBuilder.toString());

        tv_cardNum.setText("카드번호\n"+cardNumberBuilder.toString());
        //tv_cardCom.setText("카드사\n"+cardCompanyBuilder.toString());
        tv_cardCost.setText("지출금액\n"+cardCostBuilder.toString());
        tv_cardNumbyCom.setText("카드사           카드번호\n"+stringBuilder.toString());

       /* if(TODAY_KOSPI == null)
        {
            tv_kospi.setText("코스피지수 불러오는중.. 새로고침해주세요");
        }
        else tv_kospi.setText("어제 : "+YSTDAY_KOSPI+", 오늘 : "+TODAY_KOSPI+", 변화율 : "+CHANGE_RATE+"%");
*/
        return view_economy;
    }
}
