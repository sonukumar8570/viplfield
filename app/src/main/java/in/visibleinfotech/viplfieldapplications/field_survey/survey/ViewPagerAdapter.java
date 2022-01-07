package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments.Completed;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments.Created;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments.Uploaded;


class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public ViewPagerAdapter(FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new Created();
                break;
            case 1:
                fragment = new Completed();
                break;
            case 2:
                fragment = new Uploaded();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

