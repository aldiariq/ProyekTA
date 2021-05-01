package com.aldiariq.projektakripto.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aldiariq.projektakripto.R;
import com.aldiariq.projektakripto.model.FilePengguna;
import com.aldiariq.projektakripto.ui.penyimpanan.PenyimpananFragment;
import com.aldiariq.projektakripto.utils.OnDeleteClickListener;
import com.aldiariq.projektakripto.utils.OnDownloadClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilePenggunaAdapter extends RecyclerView.Adapter<FilePenggunaAdapter.ViewHolder>  implements Filterable {
    Context context;
    public ArrayList<FilePengguna> listFilepengguna;
    public ArrayList<FilePengguna> listFilepenggunatemp;

    OnDownloadClickListener onDownloadClickListener;
    OnDeleteClickListener onDeleteClickListener;

    public FilePenggunaAdapter(Context context){
        this.context = context;
        listFilepengguna = new ArrayList<>();
        listFilepenggunatemp = new ArrayList<>();
    }

    public void add(FilePengguna filePengguna){
        listFilepengguna.add(filePengguna);
        listFilepenggunatemp.add(filePengguna);
        notifyItemInserted(listFilepengguna.size() - 1);
        notifyItemInserted(listFilepenggunatemp.size() - 1);
//        PenyimpananFragment.filePenggunas = listFilepengguna;
    }

    public void addAll(ArrayList<FilePengguna> filePenggunas){
        this.clear();
        for (FilePengguna filePengguna : filePenggunas){
            add(filePengguna);
        }
    }

    public void clear(){
//        for(int i = 0; i<listFilepengguna.size();i++) {
//            listFilepengguna.remove(i);
//        }
//
//        for(int i = 0; i<listFilepenggunatemp.size();i++) {
//            listFilepenggunatemp.remove(i);
//        }


        listFilepengguna.clear();
        listFilepenggunatemp.clear();
    }

    public void setOnDownloadClickListener(OnDownloadClickListener onDownloadClickListener){
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public FilePengguna getFilepengguna(int position){
        return listFilepengguna.get(position);
    }

    public void removeFilepengguna(int position){
        listFilepengguna.remove(position);

//size == 0 here
        notifyItemRemoved(position);

//just call current.size()
        notifyItemRangeChanged(position, listFilepengguna.size());
//        if (position >= 0 && position < listFilepengguna.size()){
//            listFilepengguna.remove(position);
//        }
//        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listFilepengguna.get(position));
    }

    @Override
    public int getItemCount() {
        return listFilepengguna.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<FilePengguna> temp = new ArrayList<>();

                if(charSequence.length() == 0)
                    temp.addAll(listFilepenggunatemp);
                else
                {
                    String filtrate = charSequence.toString().toLowerCase().trim();

                    if (filtrate.length() == 0){
                        temp = listFilepenggunatemp;
                    }else {
                        for(int count = 0; count < listFilepenggunatemp.size(); count++)
                        {
                            if(listFilepenggunatemp.get(count).getNama_file().toLowerCase().contains(filtrate))
                                temp.add(listFilepenggunatemp.get(count));
                        }
                    }
                }

                PenyimpananFragment.filePenggunas = temp;
                results.values = temp;
                results.count = temp.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFilepengguna = (ArrayList<FilePengguna>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamafile;
        ImageView imgDownloadfile, imgDeletefile;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.itemfile_penyimpanan, itemView, false));
            initView();

            imgDownloadfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownloadClickListener.onDownloadClick(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            imgDeletefile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        public void bind(FilePengguna filePengguna){
            tvNamafile.setText(filePengguna.getNama_file());
        }

        private void initView(){
            tvNamafile = (TextView) itemView.findViewById(R.id.tv_namafile_itemfile);
            imgDownloadfile = (ImageView) itemView.findViewById(R.id.img_download_itemfile);
            imgDeletefile = (ImageView) itemView.findViewById(R.id.img_delete_itemfile);
        }
    }
}
