package com.example.gabriela.firecastcommunity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.fragment.OccurenceFragment;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;


public class OccurenceAdapter extends ExpandableRecyclerAdapter<OccurenceAdapter.CardOccurenceListItem> {

    public static final int TYPE_PERSON = 1001;
    Context context;

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
        ImageButton navigate, share;


        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            res = view.getContext().getResources();

            type = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__type);
            description = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__description);
            distance = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__distance);
            hour = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__hour);
            navigate = (ImageButton) itemView.findViewById(R.id.cardoccurrenceitem__navigate);


        }

        public void bind(int position) {
            super.bind(position);

            Occurrence occ = visibleItems.get(position).occurrence;

            type.setText(occ.type.name);


            // Seta a cor a partir do tipo de ocorrência
            type.setBackgroundColor(GetColorMarkerOccurrence(occ.type.id));

            share = (ImageButton) itemView.findViewById(R.id.cardoccurrenceitem__share);

            int colorText = GetColorMarkerOccurrence(occ.type.id);
            hour.setTextColor(colorText);
            description.setText(occ.description);

            if(occ.distance!=null) {
                distance.setText(MetodsHelpers.convertNumberInText(occ.distance) + " km");
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
            navigate.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(occ.latitude!=null && occ.longitude!=null) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+occ.latitude+","+occ.longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mapIntent);
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener(){

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    // context = itemView.getContext();
                    intent.setAction(Intent.ACTION_SEND);

                    String shareBody = "Ocorrência Firecast ";
                    String shareSub = "Ocorrência Firecast " + occ.type.name + "("+ occ.description+")" +
                            "\n"+ "Ocorrendo no endereço:  " +
                            (String)  occ.addressNeighborhood + " " +
                            "\n"+"Referência:  " +
                            (String) occ.addressReferencePoint + " " +
                            "\n"+"Cidade:  " +
                            (String) occ.city.name +
                            "\n"+ "Mais informações em: Firecast Comunidade";
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                    intent.putExtra(Intent.EXTRA_TEXT,shareSub);
                    intent.setType("text/html");
                    context.startActivity(Intent.createChooser(intent, "Compartilhar usando:"));

                }
            });

        }
    }

    public class OccurrenceViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView cars,location, reference, city, referenceTitle;
        Resources res;
        ImageButton details;
        View underlineReference;


        public OccurrenceViewHolder(View view) {
            super(view);

            res = view.getContext().getResources();
            cars = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__cars);
            location = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__location);
            reference = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__reference);
            city = (TextView) itemView.findViewById(R.id.cardoccurrenceitem__city);
            referenceTitle = (TextView) itemView.findViewById(R.id.cardoccurrencetitle__reference);
            underlineReference = itemView.findViewById(R.id.underline__reference);
            details  = (ImageButton) itemView.findViewById(R.id.cardoccurrenceitem__details);


        }

        public void bind(int position) {
            Occurrence occ = visibleItems.get(position).occurrence;

            location.setText(buildLocationString(occ));

            if(occ.addressReferencePoint == "" || occ.addressReferencePoint == null){
                reference.setVisibility(View.GONE);
                referenceTitle.setVisibility(View.GONE);
                underlineReference.setVisibility(View.GONE);
            } else {
                reference.setText(occ.addressReferencePoint);
                reference.setVisibility(View.VISIBLE);
            }

            if(occ.addressNumber != null) {
                location.setText(occ.adressStreet + ", nº: " + MetodsHelpers.convertDoubleInText(occ.addressNumber));
            } else {
                location.setText(occ.adressStreet);
            }

            cars.setText(res.getQuantityString(R.plurals.cars_dispatched, occ.dispatchedCars.size(), occ.dispatchedCars.size()));

            city.setText(occ.city.name);

            details.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Occurrence occurrence = GetOcurrencceFromId(occ.id);
                    if(occurrence!=null) {
                        Intent intent = new Intent(context, OccurrenceDetailsActivity.class);
                        intent.putExtra("OccurrenceKey", occurrence);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"Ocorrência não está mais ativa", Toast.LENGTH_LONG).show();
                    }
                }
            });

            details.setBackgroundResource(GetIconOccurrence(occ));


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

    private Occurrence GetOcurrencceFromId(int id_occurrence) {
        return stream(OccurenceFragment.getListOccurrence()).firstOrNull(x->x.id==id_occurrence);
    }

    private static int GetIconOccurrence(Occurrence occurrence) {

        switch (occurrence.type.id){
            case 1:
                return R.drawable.fire_icon;
            case 2:
                return R.drawable.support_icon;
            case 3:
                return R.drawable.dangerous_product_icon;
            case 4:
                return R.drawable.search_rescue_icon;
            case 5:
                return R.drawable.paramedics_icon;
            case 6:
                return R.drawable.no_service_icon;
            case 7:
                return R.drawable.other_icon;
            case 8:
                return R.drawable.car_accident_icon;
            case 9:
                return R.drawable.preventive_icon;
            case 10:
                return R.drawable.tree_cutting_icon;
            case 11:
                return R.drawable.insect_control_icon;
        }
        return R.drawable.other_pin;

    }

    private static int GetColorMarkerOccurrence(int type_id) {

        switch (type_id){
                case 1:
                    return Color.parseColor("#C06158");
                case 2:
                    return Color.parseColor("#22B2E6");
                case 3:
                    return Color.parseColor("#8EE67D");
                case 4:
                    return Color.parseColor("#55E5F2");
                case 5:
                    return Color.parseColor("#2DC4AF");
                case 6:
                    return Color.parseColor("#8c55ba");
                case 7:
                    return Color.parseColor("#FFD13E");
                case 8:
                    return Color.parseColor("#E77C71");
                case 9:
                    return Color.parseColor("#b35dac");
                case 10:
                    return Color.parseColor("#49CE42");
                case 11:
                    return Color.parseColor("#FFAA53");
            }
            return Color.parseColor("#C06158");

        }
}