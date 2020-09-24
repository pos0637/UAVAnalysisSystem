const uav = {
    state: {
        dataViewPaths: [],
        showAltitude: true,
        altitude: 100
    },
    mutations: {
        setPaths(state, paths) {
            state.dataViewPaths = paths;
        },
        addPath(state, path) {
            state.dataViewPaths.push(path);
        },
        removePath(state, item) {
            for (const path of state.dataViewPaths) {
                if (path === item) {
                    state.dataViewPaths = state.dataViewPaths.filter(p => p !== path);
                    return;
                }
            }
        },
        switchPathVisible(state, item) {
            for (const path of state.dataViewPaths) {
                if (path === item) {
                    path.visible = !path.visible;
                    return;
                }
            }
        },
        setPathColor(state, { item, color }) {
            for (const path of state.dataViewPaths) {
                if (path === item) {
                    path.color = color;
                    return;
                }
            }
        },
        switchShowAltitude(state) {
            state.showAltitude = !state.showAltitude;
        },
        setAltitude(state, altitude) {
            state.altitude = altitude;
        }
    }
};

export default uav;
