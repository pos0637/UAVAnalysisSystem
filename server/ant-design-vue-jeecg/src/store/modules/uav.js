const uav = {
    state: {
        dataViewPaths: [],
        showAltitude: true,
        altitudeScaler: 1.0,
        interpolateMode: 'none',
        interpolationScaler: 1.0,
        center: [12181.4324, 3112.6484]
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
        setAltitudeScaler(state, altitudeScaler) {
            state.altitudeScaler = altitudeScaler;
        },
        setInterpolateMode(state, mode) {
            state.interpolateMode = mode;
        },
        setInterpolationScaler(state, interpolationScaler) {
            state.interpolationScaler = interpolationScaler;
        },
        setCenter(state, item) {
            for (const path of state.dataViewPaths) {
                if (path === item) {
                    state.center = [path.points[0].lng, path.points[0].lat];
                    return;
                }
            }
        }
    }
};

export default uav;
