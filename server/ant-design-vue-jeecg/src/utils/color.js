/**
 * HEX颜色转RGBA颜色
 *
 * @export
 * @param {*} hex HEX颜色
 * @return {*} RGBA颜色
 */
export function hexToRgba(hex) {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result
        ? {
              r: parseInt(result[1], 16) / 255.0,
              g: parseInt(result[2], 16) / 255.0,
              b: parseInt(result[3], 16) / 255.0,
              a: parseInt(result[4], 16) / 255.0
          }
        : null;
}
