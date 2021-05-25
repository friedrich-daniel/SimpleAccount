package de.blocbox.simpleaccount.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder>
{
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView textViewDescription;
        public TextView textViewDate;
        public TextView textViewAmount;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
             textViewAmount = (TextView) itemView.findViewById( R.id.textViewAmount );
             textViewDescription = (TextView) itemView.findViewById( R.id.textViewDescription );
             textViewDate = (TextView) itemView.findViewById( R.id.textViewDate );

             itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(view.getContext(), TransactionActivity.class);
                intent.putExtra("AccountEntity.uid", transactions.get(position).getAccountUid());
                intent.putExtra("TransactionEntity.uid",  transactions.get(position).getUid());
                view.getContext().startActivity(intent);
            }
        }
    }

    private List<TransactionEntity> transactions;

    public TransactionsAdapter()
    {
        transactions = new ArrayList<TransactionEntity>();
    }

    public void setTransactions(List<TransactionEntity> transactions)
    {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public List<TransactionEntity> getTransactions()
    {
        return transactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate( R.layout.recycler_view_transaction_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder viewHolder, int position) {
        TransactionEntity transaction = transactions.get( position );
        viewHolder.textViewAmount.setText( String.format( Locale.ENGLISH,"%.2f", transaction.getAmount()) );
        viewHolder.textViewDescription.setText( transaction.getDescription() );
        viewHolder.textViewDate.setText( new SimpleDateFormat("yyyy-MM-dd HH:mm").format( transaction.getDate() ) );
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

}
