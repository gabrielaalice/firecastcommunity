package com.example.gabriela.firecastcommunity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


public class OccurenceAdapter extends ExpandableRecyclerAdapter<OccurenceAdapter.CardOccurenceListItem> {

    public static final int TYPE_PERSON = 1001;
    Context context;
    Image iconOcc;

    public OccurenceAdapter(Context context, List<Occurrence> orderList) {
        super(context);
        this.context = context;

        setItems(convertList(orderList));
    }

    private List<CardOccurenceListItem> convertList(List<Occurrence> orderList) {
        List<CardOccurenceListItem> listItem = new ArrayList<>();
        for (Occurrence o: orderList) {
            listItem.add(new CardOccurenceListItem(o));
            listItem.add(new CardOccurenceListItem(o,""));
        }
        return listItem;
    }

    public static class CardOccurenceListItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;
        public Occurrence occurrence;

        public CardOccurenceListItem(Occurrence occ) {
            super(TYPE_HEADER);

            Text = occ.type.name;
            occurrence = occ;
        }

        public CardOccurenceListItem(Occurrence occ, String s) {
            super(TYPE_PERSON);

            Text = occ.description;
            occurrence = occ;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView type,description, distance, hour;
        Resources res;


        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            res = view.getContext().getResources();

            type = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__type);
            description = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__description);
            distance = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__distance);
            hour = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__hour);

        }

        public void bind(int position) {
            super.bind(position);

            Occurrence occ = visibleItems.get(position).occurrence;

            type.setText(occ.type.name);

            // Seta a cor a partir do tipo de ocorrência
            type.setBackgroundColor(res.getIntArray(R.array.occurence_colors)[occ.type.getId()- ((occ.type.getId()>=6)?2:1)]);

            hour.setTextColor(res.getIntArray(R.array.occurence_colors)[occ.type.getId()- ((occ.type.getId()>=6)?2:1)]);
            description.setText(occ.description);

            if(occ.distance!=null) {
                distance.setText(MetodsHelpers.convertNumberInText("pt","BR", occ.distance) + " km");
            }else{
                distance.setText("Não foi possível calcular a distância (faltam informações)");
            }

            //String date = occ.date.substring(0,10);

            if(occ.date == null)
                hour.setVisibility(View.GONE);
            else {
                hour.setText(MetodsHelpers.convertDateTimeInString(occ.date));
                hour.setVisibility(View.VISIBLE);
            }

        }
    }

    public class OccurrenceViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView cars,location, reference, city, referenceTitle;
        Resources res;
        ImageButton navigate, share;
        Button details;
        View underlineReference;


        public OccurrenceViewHolder(View view) {
            super(view);

            res = view.getContext().getResources();
            cars = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__cars);
            location = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__location);
            reference = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__reference);
            city = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__city);
            navigate = (ImageButton) itemView.findViewById(R.id.cardoccurrenceitem__navigate);
            referenceTitle = (TextView) itemView.findViewById(R.id.cardoccurrencetitle__reference);
            underlineReference = itemView.findViewById(R.id.underline__reference);
            share = (ImageButton) itemView.findViewById(R.id.cardoccurrenceitem__share);

            share.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                   // context = itemView.getContext();
                    intent.setAction(Intent.ACTION_SEND);
                    String shareBody = "Ocorrência Firecast";
                    String shareSub = "Ocorrência ocorrendo no endereço:  " +
                            (String) location.getText() + " " +
                            (String) reference.getText() + " " + (String) city.getText() +
                             "\n"+ "Mais informações em: Firecast Comunidade";
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                    intent.putExtra(Intent.EXTRA_TEXT,shareSub);
                    intent.setType("text/plain");
                    context.startActivity(Intent.createChooser(intent, "share using:"));

                }
            });

        }

        public void bind(int position) {
            Occurrence occ = visibleItems.get(position).occurrence;

            location.setText(buildLocationString(occ));

            if(occ.addressReferencePoint != null) {
                reference.setText(occ.addressReferencePoint);
                reference.setVisibility(View.VISIBLE);
            } else {
                reference.setVisibility(View.GONE);
                referenceTitle.setVisibility(View.GONE);
                underlineReference.setVisibility(View.GONE);
            }

            cars.setText(res.getQuantityString(R.plurals.cars_dispatched, occ.dispatchedCars.size(), occ.dispatchedCars.size()));

            city.setText(occ.city.name);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.cardoccurrenceitem_header, parent));
            case TYPE_PERSON:
            default:
                return new OccurrenceViewHolder(inflate(R.layout.cardoccurrenceitem_children, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                ((OccurrenceViewHolder) holder).bind(position);
                break;
        }

    }


    private String buildLocationString(Occurrence occ) {
        return occ.adressStreet+ ", "+occ.addressNeighborhood;
    }
}