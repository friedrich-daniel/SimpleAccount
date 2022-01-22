package de.blocbox.simpleaccount.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter
        extends  RecyclerView.Adapter<AccountAdapter.ViewHolder>
        implements Filterable
{
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageViewAccountIcon;
        public TextView textViewAccountName;
        public TextView textViewAccountSum;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewAccountIcon = itemView.findViewById( R.id.imageViewAccountIcon );
            textViewAccountName = itemView.findViewById( R.id.textViewAccountName );
            textViewAccountSum = itemView.findViewById(R.id.textViewAccountSum );

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Intent intent = new Intent(view.getContext(), TransactionsActivity.class);
                intent.putExtra("AccountEntity.uid", mAccountEntityListFilter.get(position).accountEntity.getUid());
                view.getContext().startActivity(intent);
            }
        }
    }

    private List<AccountWithTransactionEntity> mAccountEntityListFilter;
    private List<AccountWithTransactionEntity> mAccountEntityListFull;

    public AccountAdapter() {
        mAccountEntityListFull = new ArrayList<>();
        mAccountEntityListFilter = new ArrayList<>();
    }

    public void setAccountWithTransactionsList(List<AccountWithTransactionEntity> accountWithTransactionsList)
    {
        mAccountEntityListFull = accountWithTransactionsList;
        mAccountEntityListFilter = new ArrayList<>( accountWithTransactionsList );
        notifyDataSetChanged();
    }

    List<AccountWithTransactionEntity> getViewData() {
        return mAccountEntityListFilter;
    }

    @NonNull
    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recycler_view_account_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(AccountAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        AccountWithTransactionEntity accountWithTransactionEntity = mAccountEntityListFilter.get(position);
        viewHolder.imageViewAccountIcon.setImageResource( Helper.GetDrawableByAccountType(accountWithTransactionEntity.accountEntity.getAccountType()));
        viewHolder.textViewAccountName.setText( accountWithTransactionEntity.accountEntity.getName() );
        viewHolder.textViewAccountSum.setText( Helper.GetSumOfTransitions(accountWithTransactionEntity.transactionEntities) );
    }

    @Override
    public int getItemCount() {
        return mAccountEntityListFilter.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AccountWithTransactionEntity> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0)
            {
                filteredList.addAll( mAccountEntityListFull );
            }else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (AccountWithTransactionEntity accountWithTransactionEntity : mAccountEntityListFull)
                {
                    String search = accountWithTransactionEntity.accountEntity.getName().toLowerCase();

                    if(search.contains(filterPattern))
                    {
                        filteredList.add( accountWithTransactionEntity );
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mAccountEntityListFilter.clear();
            mAccountEntityListFilter.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}