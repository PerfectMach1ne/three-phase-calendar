/*
 * src/utils/enums.js
 */

class Color {
  constructor(hex, name) {
    this.hex = hex;
    this.name = name;
  }

  toString() {
    return this.name;
  }

  get css() {
    return `var(--color-${this.name.toLowerCase()}, ${this.hex})`;
  }
}

export const Colors = {
  PINK: new Color("#f291eb", "Pink"),
  RED: new Color("#fa3e3d", "Red"),
  ORANGE: new Color("#f58547", "Orange"),
  YELLOW: new Color("#fde668", "Yellow"),
  LIGHT_GREEN: new Color("#b8fd68", "Light green"),
  GREEN: new Color("#41a630", "Green"),
  LIGHT_BLUE: new Color("#92fff6", "Light blue"),
  BLUE: new Color("#41b3ff", "Blue"),
  DARK_BLUE: new Color("#2e63cd", "Dark blue"),
  LAVENDER: new Color("#c493d3", "Lavender"),
  PURPLE: new Color("#8c48ff", "Purple"),
  MAGENTA: new Color("#ff34ef", "Magenta"),
  WHITE: new Color("#ffffff", "White"),
  LIGHT_GRAY: new Color("#cdcdcd", "Light gray"),
  GRAY: new Color("#818081", "Gray"),
  BLACK: new Color("#404040", "Black")
};

export const ColorUtils = {
  getRandomColor() {
    const colors = Object.values(Colors);
    return colors[Math.floor(Math.random() * colors.length)];
  },
  getTextColor(hex) {
    // Basic luminance calculation for contrast
    const r = parseInt(hex.substr(1, 2), 16);
    const g = parseInt(hex.substr(3, 2), 16);
    const b = parseInt(hex.substr(5, 2), 16);
    return (r * 0.299 + g * 0.587 + b * 0.114) > 186 
      ? Colors.BLACK 
      : Colors.WHITE;
  }
};

export class ViewType {
  static StaticTask = new ViewType("static_task", "Normal");
  static HistoricTask = new ViewType("historic_task", "Accomplished");
  static RoutineTask = new ViewType("routine_task", "Routine");

  constructor(viewtype, friendlyname) {
    this.friendlyname = friendlyname
    this.viewtype = viewtype;
  }

  toString() {
    return `ViewType.${this.viewtype}`;
  }
}