package castinder.cyrax.com.castindex.Matches;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import castinder.cyrax.com.castindex.R;

/**
 * Created by ignacioainolrivera on 09-05-18.
 */

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId;
    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
    }

    @Override
    public void onClick(View view) {

    }
}
