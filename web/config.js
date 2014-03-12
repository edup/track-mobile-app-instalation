var muzzleyConfig = {
    token: 'e8adfd0e20ccaa84',
    activity: '1ece16'
};

//Test if i'm in server side
if (typeof module != 'undefined') {
    module.exports = {
        muzzley: {
            token: process.env.MUZZLEY_APP_TOKEN || muzzleyConfig.token,
            activity: process.env.MUZZLEY_ACTIVITY || muzzleyConfig.activity
        }
    };
}