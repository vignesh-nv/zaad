package com.zaad.zaad.about;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zaad.zaad.R;

public class AboutList extends MaterialAboutActivity {

    public static MaterialAboutList createMaterialAboutList(final Context c, final int colorIcon, final int theme) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        // Add items to card

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Khanzo Play")
                .desc("© 2023 Khanzo Enterprises")
                .icon(R.drawable.app_logo)
                .build());

        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                new IconicsDrawable(c)
//                        .icon(FontAwesome.Icon.faw_info)
                        .icon(CommunityMaterial.Icon.cmd_application)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "Version",
                false));

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
//                        Intent intent = new Intent(c, AboutLicenseList.class);
//                        c.startActivity(intent);
//                        Intent intent = new Intent(c,LicenseActivity.class);
//                        c.startActivity(intent);
                    }
                })
                .build());

        appCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                        new IconicsDrawable(c)
                                .icon(CommunityMaterial.Icon.cmd_alert)
                                .color(c.getResources().getColor(R.color.white))
                                .sizeDp(18),
                        "Terms and conditions",
                        false, Uri.parse("https://zaad-cb167.web.app/terms")));

        appCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_alert)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "Privacy Policy",
                false, Uri.parse("https://zaad-cb167.web.app/privacy-policy")));

        MaterialAboutCard.Builder developedByCardBuilder = new MaterialAboutCard.Builder();
        developedByCardBuilder.title("Developed By");
//        authorCardBuilder.titleColor(ContextCompat.getColor(c, R.color.colorAccent));

        developedByCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("BAIRACORP")
                .subText("Muvendhan Information and Technologies Pvt Ltd")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_globe_model)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18))
                .build());

//        developedByCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
//                .text("Bairacorp Pvt Ltd")
//                .desc("©2023 Khanzo Enterprises")
//                .icon(new IconicsDrawable(c)
//                        .icon(CommunityMaterial.Icon.cmd_globe_model)
//                        .color(c.getResources().getColor(R.color.white))
//                        .sizeDp(18))
//                .build());

        developedByCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_email)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "Email",
                true,
                "services@bairacorp.in",
                "Contact us for project inquiry"));

        developedByCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_google)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "Website",
                true,
                Uri.parse("https://bairacorp.in/")
        ));

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Info");

//
//        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
//                new IconicsDrawable(c)
//                        .icon(CommunityMaterial.Icon.cmd_earth)
//                        .color(c.getResources().getColor(R.color.md_white_1000))
//                        .sizeDp(18),
//                "Visit Website",
//                true,
//                Uri.parse("http://daniel-stone.uk")));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_account_star)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "Rate this app",
                null
        ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_email)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "Send an email",
                true,
                "contact@khanzoplay.com",
                "Question concerning Khanzoplay App"));


        return new MaterialAboutList(appCardBuilder.build(), developedByCardBuilder.build(), convenienceCardBuilder.build());
    }

    public static MaterialAboutList createMaterialAboutLicenseList(final Context c, int colorIcon) {

        MaterialAboutCard materialAboutLIbraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(c.getResources().getColor(R.color.white))
                        .sizeDp(18),
                "material-about-library", "2016", "Daniel Stone",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard androidIconicsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Android Iconics", "2016", "Mike Penz",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard leakCanaryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "LeakCanary", "2015", "Square, Inc",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard mitLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "MIT Example", "2017", "Matthew Ian Thomson",
                OpenSourceLicense.MIT);

        MaterialAboutCard gplLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "GPL Example", "2017", "George Perry Lindsay",
                OpenSourceLicense.GNU_GPL_3);

        return new MaterialAboutList(materialAboutLIbraryLicenseCard,
                androidIconicsLicenseCard,
                leakCanaryLicenseCard,
                mitLicenseCard,
                gplLicenseCard);
    }

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        return null;
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return null;
    }
}