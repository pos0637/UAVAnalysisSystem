/**
 * HEX颜色转RGBA颜色
 *
 * @export
 * @param {*} hex HEX颜色
 * @return {*} RGBA颜色
 */
export function hexToRgb(hex) {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result
        ? {
              r: parseInt(result[1], 16),
              g: parseInt(result[2], 16),
              b: parseInt(result[3], 16),
              a: parseInt(result[4], 16) / 255
          }
        : null;
}
