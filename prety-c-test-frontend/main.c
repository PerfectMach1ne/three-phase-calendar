#define _XOPEN_SOURCE_EXTENDED 700 // An attempt at getting ncursesw to work; unnecessary thanks to cchar_t.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <locale.h>
#include <time.h>

#include <curl/curl.h>
#include <ncurses.h>

#define SMALL_Q 113

typedef struct _WIN_params {
  int height, width;
  int starty, startx;
} WIN_p;

// typedef struct _WIN_border_params {
//   cchar_t ls, rs, ts, bs,
//          tl, tr, bl, br;
// } WIN_bp;

WINDOW* create_newwin(int height, int width, int starty, int startx);
void destroy_win(WINDOW* local_win);
void draw_datestring_W(WINDOW* local_win);
void draw_monthdays_W(WINDOW* local_win);
void draw_weekdays_W(WINDOW* local_win);
void draw_hours_W(WINDOW* local_win);
void clear_top_W(WINDOW* local_win);
void clear_hours_W(WINDOW* local_win);

int main(int argc, char** argv) {
  WINDOW* filler_W; WIN_p f_p = { 7, 9, 0, 0 };
  WINDOW* datestring_W; WIN_p ds_p = { 3, 43, 0, 8 };
  WINDOW* monthdays_W; WIN_p md_p = { 3, 43, 2, 8 };
  WINDOW* weekdays_W; WIN_p wd_p = { 3, 43, 4, 8 };
  WINDOW* hours_W; WIN_p h_p = { 26, 9, 6, 0 };
  WINDOW* monday_W; WIN_p mon_p;
  WINDOW* tuesday_W; WIN_p tue_p;
  WINDOW* wednesday_W; WIN_p wed_p;
  WINDOW* thursday_W; WIN_p thu_p;
  WINDOW* friday_W; WIN_p fri_p;
  WINDOW* saturday_W; WIN_p sat_p;
  WINDOW* sunday_W; WIN_p sun_p;
  int ch;
  // int ch_mult = 0;

  CURL* curl;
  CURLcode res;

  curl = curl_easy_init();

  setlocale(LC_ALL, "");
  setlocale(LC_TIME, "en_IE.UTF-8");

  initscr();
  cbreak(); // Disable line buffering
  noecho();
  keypad(stdscr, TRUE);

  filler_W = create_newwin(f_p.height, f_p.width, f_p.starty, f_p.startx);
  datestring_W = create_newwin(ds_p.height, ds_p.width, ds_p.starty, ds_p.startx);
  monthdays_W = create_newwin(md_p.height, md_p.width, md_p.starty, md_p.startx);
  weekdays_W = create_newwin(wd_p.height, wd_p.width, wd_p.starty, wd_p.startx);
  hours_W = create_newwin(h_p.height, h_p.width, h_p.starty, h_p.startx);

  while ( (ch = getch()) != SMALL_Q ) {
    if ( filler_W != NULL ) destroy_win(filler_W);
    if ( datestring_W != NULL ) destroy_win(datestring_W);
    if ( monthdays_W != NULL ) destroy_win(monthdays_W);
    if ( weekdays_W != NULL ) destroy_win(weekdays_W);
    if ( hours_W != NULL ) destroy_win(hours_W);
    
    filler_W = create_newwin(f_p.height, f_p.width, f_p.starty, f_p.startx);
    datestring_W = create_newwin(ds_p.height, ds_p.width, ds_p.starty, ds_p.startx);
    monthdays_W = create_newwin(md_p.height, md_p.width, md_p.starty, md_p.startx);
    weekdays_W = create_newwin(wd_p.height, wd_p.width, wd_p.starty, wd_p.startx);
    hours_W = create_newwin(h_p.height, h_p.width, h_p.starty, h_p.startx);

    cchar_t ls; setcchar(&ls, L"║", 0, 0, NULL);
    cchar_t rs; setcchar(&rs, L"║", 0, 0, NULL);
    cchar_t ts; setcchar(&ts, L"═", 0, 0, NULL);
    cchar_t bs; setcchar(&bs, L"═", 0, 0, NULL);
    cchar_t tl; setcchar(&tl, L"╔", 0, 0, NULL);
    cchar_t tr; setcchar(&tr, L"╗", 0, 0, NULL);
    cchar_t bl; setcchar(&bl, L"╚", 0, 0, NULL);
    cchar_t br; setcchar(&br, L"╝", 0, 0, NULL);
    wborder_set(filler_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(filler_W);
    setcchar(&tl, L"╦", 0, 0, NULL);
    wborder_set(datestring_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(datestring_W);
    setcchar(&tl, L"╠", 0, 0, NULL);
    setcchar(&tr, L"╣", 0, 0, NULL);
    wborder_set(monthdays_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(monthdays_W);
    setcchar(&bl, L"╩", 0, 0, NULL);
    wborder_set(weekdays_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(weekdays_W);
    setcchar(&tr, L"╬", 0, 0, NULL);
    setcchar(&bl, L"╚", 0, 0, NULL);
    wborder_set(hours_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(hours_W);

    draw_datestring_W(datestring_W);
    wrefresh(datestring_W);
    draw_monthdays_W(monthdays_W);
    wrefresh(monthdays_W);
    draw_weekdays_W(weekdays_W);
    wrefresh(weekdays_W);
    draw_hours_W(hours_W);
    wrefresh(hours_W);
    move(0,0);
    refresh();
  }
  
   // Clean up windows after the loop breaks
  clear_top_W(datestring_W);
  clear_top_W(monthdays_W);
  clear_top_W(weekdays_W);
  clear_hours_W(hours_W);
  
  destroy_win(filler_W);
  destroy_win(datestring_W);
  destroy_win(monthdays_W);
  destroy_win(weekdays_W);
  destroy_win(hours_W);

  if (curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://127.0.0.1:8057/teapot");
    /* Do not do the transfer - only connect to host */
    // curl_easy_setopt(curl, CURLOPT_CONNECT_ONLY, 1L);

    res = curl_easy_perform(curl);

    if (res != CURLE_OK) {
      printw("curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
    } else {
      char buf[512];
      size_t nread;
      long sockfd;
      char *ct;
      res = curl_easy_getinfo(curl, CURLINFO_CONTENT_TYPE, &ct);
      
      // this doesn't work lol
      if ((CURLE_OK == res) && ct) {
        printw("Wondrous! We have received Content-Type %s!\n", &ct);
      }

      res = curl_easy_getinfo(curl, CURLINFO_ACTIVESOCKET, &sockfd);

      res = curl_easy_recv(curl, buf, sizeof(buf), &nread);
      printw("%s", &buf);
    }

    curl_easy_cleanup(curl);
  }

  refresh();
  getch(); // debug

  endwin();
  return EXIT_SUCCESS;
}

WINDOW* create_newwin(int height, int width, int starty, int startx) {
  WINDOW* local_win;
  
  local_win = newwin(height, width, starty, startx);
  box(local_win, 0, 0);
  wrefresh(local_win); // Refresh the window to show the bocx

  return local_win;
}

void destroy_win(WINDOW* local_win) {
  cchar_t clr; setcchar(&clr, L" ", 0, 0, NULL);
  wborder_set(local_win, &clr, &clr, &clr, &clr, &clr, &clr, &clr, &clr);
  wrefresh(local_win);
  delwin(local_win);
}

void draw_datestring_W(WINDOW* local_win) {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[24];

  // Prepare the English format string.
  char fstr[11] = "%e";
  switch (lt->tm_mday % 10) {
    case 1:
      if (lt->tm_mday == 11 && lt->tm_mday < 10) break;
      strcat(fstr, "st %B %Y");
      break;
    case 2:
      if (lt->tm_mday == 12 && lt->tm_mday < 10) break;
      strcat(fstr, "nd %B %Y");
      break;
    case 3:
      if (lt->tm_mday == 13 && lt->tm_mday < 10) break;
      strcat(fstr, "rd %B %Y");
      break;
    default:
      strcat(fstr, "th %B %Y");
  }
  
  if (strftime(tstr_buf, sizeof(tstr_buf), fstr, lt)) {
    mvwprintw(local_win, 1, 7, (lt->tm_mday < 10 ? "Today is%s!" : "Today is %s!"), &tstr_buf);
    wmove(local_win, 0, 0);
  } else { mvwprintw(local_win, 1, 7, "strftime failed"); }
}

void draw_monthdays_W(WINDOW* local_win) {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[7];

  // For testing if it doesn't get wrecked on Sundays and monthdays > 7:
  // lt->tm_mday += 8 % 31; lt->tm_wday += 8 % 7;
  lt->tm_mday = (lt->tm_mday - lt->tm_wday % 7 + 1) % 31; // Potentially flawed on [28,30] monthday months.
  mktime(lt);
  for (int i = 0; i < 7; i++) {
    /* https://pubs.opengroup.org/onlinepubs/009695399/functions/mktime.html
     * "The original values of the tm_wday and tm_yday components of the structure are ignored, [...]"" 
     * ...can you guess what I tried to do before?
    */
    lt->tm_mday = (i != 0 ? lt->tm_mday + 1 : lt->tm_mday) % 31; // Potentially flawed on [28,30] monthday months.

    time_t newt = mktime(lt);
    lt = localtime(&newt);

    if (strftime(tstr_buf, sizeof(tstr_buf), "%d.%m", lt)) {
      mvwprintw(local_win, 1, 1 + i*6, "%s║", &tstr_buf);
    } else { mvwprintw(local_win, 1, 1, "strftime failed"); }
  }
}

void draw_weekdays_W(WINDOW* local_win) {
  char* weekdays[7] = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

  for (int j = 0; j < sizeof(weekdays) / sizeof(char*); j++) { 
    mvwprintw(local_win, 1, 1 + j*6, (j!=6?" %s ║":" %s "), weekdays[j]); 
  }
  // wmove(local_win, 1, 1);
}

void clear_top_W(WINDOW* local_win) {
  wmove(local_win, 1, 0);
  wclrtoeol(local_win);
}

void draw_hours_W(WINDOW* local_win) {
  char hstr_buf[] = "00:00"; // Has to be an array (char[]) as opposed to char*!!!
  for (int i = 0; i < 24; i++) {
    if (i < 10) {
      hstr_buf[0] = 48;
      hstr_buf[1] = 48 + i;
    } else {
      hstr_buf[0] = (int)(48 + (i - i % 10) / 10);
      hstr_buf[1] = 48 + i % 10;
    }
    mvwprintw(local_win, 1 + i, 1, " %s", hstr_buf);
  }
}

void clear_hours_W(WINDOW* local_win) {
  for (int i = 0; i < 24; i++) {
    wmove(local_win, 1 + i, 1);
    wclrtoeol(local_win);
  }
}